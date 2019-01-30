import java.util.Scanner;
import java.util.regex.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


class imageEditor{
	public static String[] commandList = new String[]{
		"[1]: Covert image to greyscale and save as copy.",
		"[2]: Edge detection on image and save as copy. ",
		"[3] Testing NxN colour Grid",
		"[4]: Select a new file ",
		"[5]: Run Kirsh Edge Detection ",
		"[9]: Exit the program "	
	};
	public static String[] formatPrintList = new String[]{
		"[1]: .jpg",
		"[2]: .png ",
	};
		public static String[] formatsList = new String[]{
		"jpg",
		"png",
	};
	
	
	//Main Method
	public static void main (String args[]){
		//Scanner to be used
		String inputString;
		BufferedImage imageToEdit;
		Scanner input = new Scanner(System.in);
		boolean select = false;
		 //loop forever
		while(true){
			
			File imageFile = newFileSelection();
			System.out.println("File '" + imageFile  + "' Selected. Select edit mode by choosing number:");
			printStringArray(commandList);
			inputString = input.nextLine();
				
			if(inputString.equals("1")) {
				String fileName = outputFileNamer(input);
				String fileFormat = outputFileFormat(input);
				imageToEdit = ImageManipulate.readImage(imageFile);
				ImageManipulate.convertToGreyScale(imageToEdit, fileName, fileFormat);
				System.out.println("Finished processing image");
			}
			else if(inputString.equals("2")){ 
				imageToEdit = ImageManipulate.readImage(imageFile);
				int[][] grid = ImageManipulate.getPixelGrid3x3(imageToEdit, 0, 0);
				print2DIntArray(grid);

			}
			else if(inputString.equals("3")) {
				imageToEdit = ImageManipulate.readImage(imageFile);
				int[][] grid = ImageManipulate.getPixelGridNxN(imageToEdit, 20, 20, 0 ,0);
				print2DIntArray(grid);
				System.out.println("Colours Array Finished");
				System.out.println("Finished processing image");
				grid = ImageManipulate.getPixelGridNxNCentered(imageToEdit, 20, 20, 10 ,10);
				print2DIntArray(grid);
			}
			else if(inputString.equals("4")) {
				continue;
			}
			else if(inputString.equals("5")) {
				String fileName = outputFileNamer(input);
				String fileFormat = outputFileFormat(input);
				imageToEdit = ImageManipulate.readImage(imageFile);
				select = selectBoolean(input);
				ImageManipulate.kirschEdgeDetect(imageToEdit,select, fileName, fileFormat);
				System.out.println("Finished processing image");
				
			}
			else if(inputString.equals("9")) {
				System.exit(0);
			}		
		}
	}
		
	
	public static File newFileSelection(){
		String inputString;
		Scanner input = new Scanner(System.in);
		System.out.println("Please Enter the name of the file you wish to edit: ");
		Matcher m;
		while(true){
			inputString = input.nextLine();
			quitValidation(inputString, input);
			try{
			File imageFile = new File(inputString);
				if(imageFile.exists()){	
					return imageFile;	
				}
				else{
					System.out.println("Please select a valid file");
				}
			}
			catch(Exception e){
				System.out.println("Error: " + e);
			}	
		}		
	}
	
	
	public static boolean selectBoolean(Scanner input){
		String inputString;
		System.out.println("Do you want a strong edge magnitude value: Press '1' for Yes and '2' for No ");
		Matcher m;
		while(true){
			inputString = input.nextLine();
			if(inputString.equals("1")){
				return true;
			}
			else if(inputString.equals("2")){
			    return false;
			}
			else{
				quitValidation(inputString, input);
			}
		}		
	}
	
	
	public static void quitValidation(String stringToTest, Scanner input){
		Pattern p = Pattern.compile("(?i)(exit|quit)");
		Matcher m;
		m = p.matcher(stringToTest);
		if(m.matches()){
			//Give final prompt
			System.out.println("Are you sure you want to quit ? [Y/N]");
			//get the conformation string
			stringToTest = input.nextLine();
			//if that results in a conformation, exit the program
			if(stringToTest.equals("YES") ||  stringToTest.equals("Y") || stringToTest.equals("yes") || stringToTest.equals ("y")) {
				System.exit(0);
			}
		}
		else{
			return;
		}
	}	
	
	public static void printStringArray(String[] stringArray){
		
		for(int i = 0; i < stringArray.length; i++){
			System.out.println(stringArray[i]);
		}			
	}
	public static void printIntArray(int[] intArray){
		for(int i = 0; i < intArray.length; i++){
			System.out.print(intArray[i] + "\t");
		}			
	}
	public static void print2DIntArray(int[][] int2DArray){
		System.out.println("----------------------");
		for(int i = 0; i < int2DArray.length; i++){
			 printIntArray(int2DArray[i]);	
			 System.out.println("");
		}	
	}
	

	
	public static String outputFileNamer(Scanner input){
		String inputString;
		String fileName;
		System.out.println("Choose name for output file");
		inputString = input.nextLine();
		fileName = inputString;	
		return fileName;
	}
	
		public static String outputFileFormat(Scanner input){
		String inputString;
		String fileFormat;
		System.out.println("Choose format for output file");
		printStringArray(formatPrintList);
		inputString = input.nextLine();
		fileFormat = formatsList[Integer.parseInt(inputString) - 1];		
		return fileFormat;
	}
	
}

class ImageManipulate{
	
	public static int[][][] kirschFiltersWeakMagnitude = 
		{ 
			{
				{-1 , 0 ,1},
				{-2 , 0 ,2},
				{-1 , 0 ,1}
			},
			{
				{-2 , -1 ,0},
				{-1 , 0 ,1 },
				{0 , 1 , 2 }
			},
			{
				{-1 , -2 ,-1},
				{0 , 0 ,0},
				{1 , 2 ,1}
			},
			{
				{0 , -1 ,-2},
				{1 , 0 ,-1},
				{2 , 1 ,0}
			},
			{
				{1 , 0 ,-1},
				{2 , 0 ,-2},
				{1 , 0 ,-1}
			},		
			{
				{2 , 1 ,0},
				{1, 0 ,-1},
				{0 , -1 ,-2}
			},
			{
				{1 , 2 ,1},
				{0 , 0 ,0},
				{-1 , -2 ,-1}
			},
			{
				{0 , 1 ,2},
				{-1 , 0 ,1},
				{-2 , -1 ,0}
			}
		}; 
		
		
		
			public static int[][][] kirschFiltersStrongMagnitude = 
		{ 
			{
				{5 , -3 ,-3},
				{5 , 0 ,-3},
				{5 , -3 ,-3}
			},
			{
				{5 , 5 ,-3},
				{5 , 0 ,-3 },
				{-3 , -3 , -3 }
			},
			{
				{5 , 5 ,5},
				{-3 , 0 ,-3},
				{-3 , -3 ,-3}
			},
			{
				{-3 , 5 ,5},
				{-3 , 0 ,5},
				{-3 , -3 ,-3}
			},
			{
				{-3 , -3 ,5},
				{-3 , 0 ,5},
				{-3 , -3 ,5}
			},		
			{
				{-3 , -3 ,-3},
				{-3, 0 ,5},
				{-3 , 5 ,5}
			},
			{
				{-3 , -3 ,-3},
				{-3 , 0 ,-3},
				{5 , 5 ,5}
			},
			{
				{-3 , -3 ,-3},
				{5 , 0 ,-3},
				{5 , 5 ,-3}
			}
		}; 
		
		
	
		
	
	public static BufferedImage readImage(File imageFile){
		BufferedImage image = null;
		try{
			image = ImageIO.read(imageFile);
			return image;
		}
		catch(Exception e){
			System.out.println("Error: " + e);
			System.out.println("Error With File aborting convert to greyscale");
		}
		return image;
	}
	
	
	public static int getGreyPixelValue(BufferedImage image, int x, int y ){
		int colour = image.getRGB(x,y);
		int red = (colour >> 16) & 0x000000FF;
		int green = (colour >> 8 ) & 0x000000FF;
		int blue = (colour) & 0x000000FF;
		int grey = (red + green + blue) /3 ;
		return grey;	
	}
	public static void setPixelValue(BufferedImage image, int x, int y, int red, int green, int blue ){
		
		int colour =  red << 16 | green << 8 | blue;
		image.setRGB(x,y,colour);
	}
	
	
	

	public static void convertToGreyScale(BufferedImage image, String fileName, String fileFormat){
		//Initalise Images
		BufferedImage EditedImage = null;
		int grey;
		int colour;
		try{
			//Read in the original image
			//Create a new image that we will save
			EditedImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_GRAY);

			//loop thru all pixels and get their RGB colour, then save it in the other edited image
			
			for(int x = 0; x < image.getWidth(); x++){
				for(int y = 0; y < image.getHeight(); y++){
					
					grey = getGreyPixelValue(image, x, y);
					
					//colour =  grey << 16 | grey << 8 | grey;
					setPixelValue(EditedImage,x,y,grey,grey,grey);
					//EditedImage.setRGB(x,y,colour);		
				}	
			}
			//Create a new output file
			File newImage = new File(fileName + "." + fileFormat);
			ImageIO.write(EditedImage,fileFormat, newImage);
			
			
		}catch(Exception e){
			System.out.println("Error: " + e);
			System.out.println("Error With File aborting convert to greyscale");
		}
		
		
	}
		/*Takes in a buffered image and returns a 3x3 grid of surrounding pixels, BufferedImage arguemnt ma have to take an ImageIO.read(), 
		This method is essentally "getPixelGridNxNCentered(...,3,3,...,...)"  but is used specfically for the Kirsh Filters
		*/
		public static int[][] getPixelGrid3x3(BufferedImage image, int x, int y){
			int[][] pixelValueArray = new int[3][3];
			try{
				
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						if(x-1+i < 0 || y-1+j < 0 || x-1+i >= image.getWidth() || y-1+j >= image.getHeight()){
							pixelValueArray[i][j] = 0;
						}
						else{
							//pixelValueArray[i][j] = image.getRGB(x-1+i,y-1+j);
							pixelValueArray[i][j] = getGreyPixelValue(image, x-1+i, y-1+j);
						}
					}	
				}
				
				return pixelValueArray;
			}
			catch(Exception e){
				System.out.println("Error: " + e);
				System.out.println("Error With File aborting convert to greyscale");
			}
			return pixelValueArray;
	}	
	
	
	/*Takes a X,Y coordinates, a Buffered image and a row and coloum size, it returns a 2D array of the pixel starting at that X and Y Value*/
			public static int[][] getPixelGridNxN(BufferedImage image, int rowNumber, int colNumber , int x, int y){
			int[][] pixelValueArray = new int[rowNumber][colNumber];
			try{
				
				for(int i = 0; i < rowNumber; i++){
					for(int j = 0; j < colNumber; j++){
						if(x-1+i < 0 || y-1+j < 0 || x-1+i >= image.getWidth() || y-1+j >= image.getHeight()){
							pixelValueArray[i][j] = 0;
						}
						else{
							//pixelValueArray[i][j] = image.getRGB(x-1+i,y-1+j);
							pixelValueArray[i][j] = getGreyPixelValue(image, x-1+i, y-1+j);
						}
					}	
				}
				
				return pixelValueArray;
			}
			catch(Exception e){
				System.out.println("Error: " + e);
				System.out.println("Error With File aborting convert to greyscale");
			}
			return pixelValueArray;
	}
	
	
	/*Takes a X,Y coordinates, a Buffered image and a row and coloum size, it returns a 2D array of the pixel values surrounding the X and Y which will be at the center if the array*/
	public static int[][] getPixelGridNxNCentered(BufferedImage image, int rowNumber, int colNumber , int x, int y){
			int[][] pixelValueArray = new int[rowNumber][colNumber];
			int xCenter = colNumber/2;
			int yCenter = rowNumber/2;
			try{
				
				for(int i = 0; i < rowNumber; i++){
					for(int j = 0; j < colNumber; j++){
						if(x-xCenter+i < 0 || y-yCenter+j < 0 || x-xCenter+i >= image.getWidth() || y-yCenter+j >= image.getHeight()){
							pixelValueArray[i][j] = 0;
						}
						else{

							//pixelValueArray[i][j] = image.getRGB(x-xCenter+i,y-yCenter+j);
							pixelValueArray[i][j] = getGreyPixelValue(image, x-xCenter+i, y-yCenter+j);
							
						}
					}	
				}
				
				return pixelValueArray;
			}
			catch(Exception e){
				System.out.println("Error: " + e);
				System.out.println("Error With File aborting convert to greyscale");
			}
			return pixelValueArray;
	}	
	

	
	    /*This method was intended to give a border of Zeros around the Image, but Should not be used as it it no longer needed*/
		public static int[][] getPixelGridNxNZeroBorder(BufferedImage image, int rowNumber, int colNumber , int x, int y){
			int[][] pixelValueArray = new int[rowNumber + 1][colNumber + 1];
			try{
				
				for(int i = 0; i < rowNumber; i++){
					for(int j = 0; j < colNumber; j++){
						if(x-1+i < 0 || y-1+j < 0 || x-1+i >= image.getWidth() || y-1+j >= image.getHeight()){
							pixelValueArray[i][j] = 0;
						}
						else{
							pixelValueArray[i][j] = image.getRGB(x-1+i,y-1+j);
						}
					}	
				}
				
				return pixelValueArray;
			}
			catch(Exception e){
				System.out.println("Error: " + e);
				System.out.println("Error With File aborting convert to greyscale");
			}
			return pixelValueArray;
	}	
	
	/*Multiply an array by a corrispoing array, only tested with arrays of  same size*/
	public static int multiply2DArrayBy2DArray(int[][] array1, int[][]array2){
		int sum = 0; 
		for(int i =0; i < array1.length; i++){
			for(int j =0; j < array1.length; j++){
					sum += array1[i][j] * array2[i][j];
				
			}	
		}
		return sum;
	}	

	/*Use greyscale image for optimal results*/
	public static void kirschEdgeDetect(BufferedImage image, boolean strength , String fileName, String fileFormat){
		BufferedImage EditedImage = null;
		int maxOutValue = 0;
		int imageGrid[][];
		int valueOut = 0;
		try{
			EditedImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
			
			
			
				for(int imageX = 0; imageX < image.getWidth(); imageX++){
					
					for(int imageY = 0; imageY < image.getHeight(); imageY++){
						
						
						maxOutValue = 0;
						imageGrid = getPixelGrid3x3(image, imageX, imageY);
						if(strength == false){
							for(int i=0; i < 8; i++){
								valueOut = multiply2DArrayBy2DArray(kirschFiltersWeakMagnitude[i],imageGrid);
								if(valueOut >= maxOutValue){
									maxOutValue = valueOut;
								}

							}
						}else{
								for(int i=0; i < 8; i++){
									
								valueOut = multiply2DArrayBy2DArray(kirschFiltersStrongMagnitude[i],imageGrid);
								if(valueOut >= maxOutValue){
									maxOutValue = valueOut;
								}
							}

						}
						//if(valueOut >= maxOutValue){
						//maxOutValue = valueOut;
						//}
						maxOutValue = maxOutValue + 127;
						if(maxOutValue < 0 ){
							maxOutValue = 0;
						}
						else if(maxOutValue > 255){
							maxOutValue = 255;
						}
						setPixelValue(EditedImage, imageX, imageY, maxOutValue, maxOutValue, maxOutValue );
						//EditedImage.setRGB(imageX,imageY,maxOutValue);				
				}
			}
			File newImage = new File(fileName + "." + fileFormat);
			ImageIO.write(EditedImage,fileFormat, newImage);	
	}
	catch(Exception e){
		System.out.println("Error: " + e);
	}
	
	}
	
}