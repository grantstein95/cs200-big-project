import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class PA2 {
	static boolean endFiles = false;
	static webPages webPages = new webPages();

	public static void main(String[] args) {
		String eof = "*EOFs*";
		try {
			Scanner readInputFile = new Scanner(new File(args[0]));

			while(endFiles == false){ 
				String exampleName = readInputFile.next();
				if(exampleName.equals(eof)){
					endFiles = true;
				}else{
					webPages.addPage(exampleName);
				}
			}//end while

			webPages.printTerms();

			int numStopWords = readInputFile.nextInt();	
			webPages.pruneStopWords(numStopWords);
			webPages.printTerms();

			while(readInputFile.hasNext()){
				String tempString = readInputFile.next().toLowerCase();
				String[] temp = webPages.whichPages(tempString);
				if(temp != null){
					System.out.print(tempString + " in pages: ");
					for(int i = 0; i < temp.length; i++){
						System.out.print(temp[i] + ", "); 
					}
					System.out.println();
				}else{
					System.out.println(tempString + " not found");
				}
			}
			
			readInputFile.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
}
