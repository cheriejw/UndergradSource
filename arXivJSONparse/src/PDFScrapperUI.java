import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/*
 * User-friendly program for PDFScrapper.
 */

public class PDFScrapperUI {
	public static void main(String[]args) throws IOException{
		//Initializations
		Scanner keyboard = new Scanner(System.in);
		String pdfFileLoc, textFilesDir, input;
		PDFScrapper scrape;
		
		//User Input
		System.out.println("Make no mistake, I don't check for stupid people.");
		System.out.println("Please input the directory the PDF files are located with '\\' at the end: ");
		pdfFileLoc = keyboard.nextLine();
		System.out.println("Please input the directory destination of the scraped text with '\\' at the end: ");
		textFilesDir = keyboard.nextLine();
		scrape = new PDFScrapper(Paths.get(pdfFileLoc), textFilesDir);
		
		scrape.logWanted();
		scrape.printWanted();
		while(true){
			System.out.println("Please copy and paste the directory the PDF file you wish to remove, "
					+ "enter 'none' if you are content with the files to be scrapped. ");
			input = keyboard.nextLine();
			if (input.equals("none")){
				break;
			}
			scrape.removeFromWanted(Paths.get(input).toAbsolutePath());
		}
		scrape.convert();
		keyboard.close();
	}
}
