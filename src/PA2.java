import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class PA2 {
	private static boolean endFiles = false;
	private static WebPages webPages = new WebPages();
	private static final String EOF_FLAG = "*EOFs*";

	public static void main(String[] args) {
		try {
			Scanner readInputFile = new Scanner(new File(args[0]));

			while (endFiles == false){ 
				String exampleName = readInputFile.next();
				if (exampleName.equals(EOF_FLAG)) {
					endFiles = true;
				} else {
					webPages.addPage(exampleName);
				}
			}

			webPages.printTerms();

			int numStopWords = readInputFile.nextInt();	
			webPages.pruneStopWords(numStopWords);
			webPages.printTerms();

			while (readInputFile.hasNext()) {
				String tempString = readInputFile.next();
				String[] temp = webPages.whichPages(tempString);
				if(temp != null){
					System.out.print(tempString + " in pages: ");
					for (int i = 0; i < temp.length; i++) {
						System.out.print(temp[i]);
						if (i != temp.length - 1)
							System.out.print(", ");
					}
					System.out.println();
				} else {
					System.out.println(tempString + " not found");
				}
			}
			
			readInputFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			e.printStackTrace();
		}
	}
}
