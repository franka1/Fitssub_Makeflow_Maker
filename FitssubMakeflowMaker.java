import java.io.File;
import java.io.FileNotFoundException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.Date;

public class FitssubMakeflowMaker {
	public static int makePairs(ArrayList<String> images, int index, int subIndex) {
		int size = index;
		String firstDark = images.get(0);
		String lastDark = images.get(size - 1);
		for(int i = 1; i < size/2 + 1; i++) {
			String science = images.get(i);
			System.out.println("fitssub_output" + subIndex + ".fits: " + science + " " + firstDark);
			System.out.println("\tfitssub -i " + science + " -r " + firstDark + " -o fitssub_output" + subIndex + ".fits\n");
			subIndex++;
		}
		for(int i = size/2 + 1; i < size - 1; i++) {
			String science = images.get(i);
			System.out.println("fitssub_output" + subIndex + ".fits: " + science + " " + lastDark);
			System.out.println("\tfitssub -i " + science + " -r " + firstDark + " -o fitssub_output" + subIndex + ".fits\n");
			subIndex++;
		}
		return subIndex;
	}
	public static int makePairs2(ArrayList<String> images, int index, int subIndex) {
		//This is a modification of makePairs to deal with an input file
		//whose last line is a Science image
		int size = index;
		String firstDark = images.get(0);
		for(int i = 1; i < size; i++) {
			String science = images.get(i);
			System.out.println("fitssub_output" + subIndex + ".fits: " + science + " " + firstDark);
			System.out.println("\tfitssub -i " + science + " -r " + firstDark + " -o fitssub_output" + subIndex + ".fits\n");
			subIndex++;
		}
		return subIndex;
	}
	public static void main(String[] args) throws FileNotFoundException {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-M hh:mm:ss.nnnnnn");
		
		/* Write Header information (if needed) */
		//System.out.println("HEADER STUFF HERE\n");
		
		File input = new File(args[0]);
		Scanner fileScan = new Scanner(input);
		
		
		
		//ArrayList<DatedObject> images = new ArrayList();
		ArrayList<String> images = new ArrayList<String>();
		images.add("Placeholder");
		
		while(fileScan.hasNextLine()) {
			
			//Scan until we hit the first Dark image
			String currentImage = fileScan.nextLine();
			int firstComma = currentImage.indexOf(',');
			int secondComma = currentImage.indexOf(',', firstComma + 1);
			//int thirdComma = currentImage.indexOf(',', secondComma + 1);
			if(currentImage.charAt(secondComma + 1) == 'D') {
				/*//Get the Date object
				String rawDate = currentImage.substring(firstComma, secondComma);
				Date date = sdf.parse(rawDate);
				
				//Make the DatedObject
				DatedObject firstDark = new DatedObject(date, currentImage.substring(0, firstComma), currentImage.substring(secondComma + 1, thirdComma));
				*/
				
				String firstDark = currentImage.substring(0, firstComma);
				images.set(0, firstDark);
				
				//Start counting
				int index = 1;
				int subIndex = 1;
				while(fileScan.hasNextLine()) {
					currentImage = fileScan.nextLine();
					firstComma = currentImage.indexOf(',');
					secondComma = currentImage.indexOf(',', firstComma + 1);
					//thirdComma = currentImage.indexOf(',', secondComma + 1);
					
					/*rawDate = currentImage.substring(firstComma, secondComma);
					date = sdf.parse(rawDate);
					DatedObject image = new DatedObject(date, currentImage.substring(0, firstComma), currentImage.substring(secondComma + 1, thirdComma));
					*/
					
					String image = currentImage.substring(0, firstComma);
					while(images.size() <= index) {
						images.add("Placeholder");
					}
					images.set(index, image);
					
					index++;
					//If the image we just added is a dark, we have some more processing to do.
					if(currentImage.charAt(secondComma + 1) == 'D') {
						if(index > 1) { //Check that we have at least one Science image
							subIndex = makePairs(images, index, subIndex);
						}
						index = 0;
					}
				}
				subIndex = makePairs2(images, index, subIndex);
				
				//Done. All loops should exit now.
			}
		}
	}
	//This whole class may be unnecessary, but will be useful if our data is not pre-sorted
	/*public class DatedObject {
		private Date date;
		private String filename;
		private String type;
				
		public DatedObject(Date aDate, String aFilename, String aType) {
			date = aDate;
			filename = aFilename;
			type = aType;
		}
				
		public Date getDate() {
			return date;
		}
				
		public String getFile() {
			return filename;
		}
				
		public String getType() {
			return type;
		}
	}*/
}