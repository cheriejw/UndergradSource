import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/*
 * The PDFScrapper class has a master HashMap that will be the size of the number of PDF files.
 * Given a directory (containing PDF files) this object will scrape the text and save it into a HashMap.
 * 
 * If you want to save to RAM instead of to file use private HashMap <String, String> master as <Identifier, Body>.
 *
 * I need to change this so that it takes out the space escapes and line escapes. 
 *
 */

public class PDFScrapper {
	
	//Private Class Variables
	private HashSet <Path> wanted; //All of the file identifiers you want to save to master.
	private Path directory;
	private String writepath;
	
	//Parsing Method Variables
	private PDFParser parser;
	private PDFTextStripper pdfStripper;
	private PDDocument pdDoc;
	private COSDocument cosDoc;
	 
	private String text;
	private File file;
	
	//Constructor.
	public PDFScrapper(){
		//Instantiating variables.
		wanted = new HashSet <Path>();
	}
	
	//Overloaded constructor.
	public PDFScrapper(Path dir, String wpath){
		//Instantiating variables.
		wanted = new HashSet <Path>();
		setDir(dir);
		setWritePath(wpath);
	}
	
	/*
	 * This method will turn the PDF into a String of text.
	 * input: the filePath String of the file you want to scrape.
	 * output: the String what is the body of the text.
	 */
	 public String scrape(String filePath) throws IOException {
		 this.pdfStripper = null;
		 this.pdDoc = null;
		 this.cosDoc = null;
		 
		 file = new File(filePath); //Make a new File object with the Path
		 parser = new PDFParser(new RandomAccessFile(file,"r")); //Give PDFParser object the File.
		 pdfStripper = new PDFTextStripper();
		 
		 parser.parse();
		 cosDoc = parser.getDocument(); //COSDocument Object getting the Document from the loaded PDFParser
		 pdDoc = new PDDocument(cosDoc); //PDDocument made with COSDocument
		 pdDoc.getNumberOfPages(); //PDDocument Object
		 pdfStripper.setStartPage(1); //Set PDFTextStripper Object to start page
		 pdfStripper.setEndPage(pdDoc.getNumberOfPages()); //Set PDFTextStripper Object to end page
		 
		 text = pdfStripper.getText(pdDoc);
		 return text;
	 }	
	 
	 /*
	  * This method will walk through the set directory and scrap the PDF.
	  * It will also clean the text in the PDF files to take out all or most of the junk
	  * before the abstract or introduction. 
	  * 
	  * Uses the wanted HashMap to scrape the files you want.
	  */
	 public void convert() throws IOException{
		   Path pdfPath = directory.toAbsolutePath();
		   printWanted();
	       Files.walkFileTree(pdfPath, new SimpleFileVisitor<Path>() {
	    	   
	           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	              if (pdfPath.equals(dir)) {
	                 return FileVisitResult.CONTINUE;
	              }
	              return FileVisitResult.SKIP_SUBTREE;
	           }

	           public FileVisitResult visitFile(Path file,
	            BasicFileAttributes attrs) throws IOException {
	              if (wanted.contains(file)) {
	            	  write(file, cleanBody(scrape(file.toString())));
	              }
	              return FileVisitResult.CONTINUE;
	           }
	           public FileVisitResult visitFileFailed(Path file, IOException e) {
	              return FileVisitResult.CONTINUE;
	           }
	        });
	       System.out.println("Files from wanted HashSet successfully written to " + writepath +".");
	 }
	   
	   /*
	    * This method will give you the identifier String.
	    * input: the Path of the file or document you want the id for.
	    * output:
	    */
	   public String identifier(Path file){
	    	String temp = ("oai:arXiv.org:" + file.getFileName().toString().replace(".pdf", ""));//replace("000", "/000")
	    	return temp.substring(0, temp.length()-7)+ "/" + temp.substring(temp.length()-7);
	   }
	   
	   /*
	    * This method will just run through the files and get all the file titles.
	    * Then you can use removefromwanted method to take of unwanted files.
	    * So you can compare and pick which ones you want.
	    */
	   public void logWanted() throws IOException{
		   Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
	           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	              if (directory.equals(dir)) {
	                 return FileVisitResult.CONTINUE;
	              }
	              return FileVisitResult.SKIP_SUBTREE;
	           }

	           public FileVisitResult visitFile(Path file,
	            BasicFileAttributes attrs) {
	              if (file.toString().endsWith(".pdf")) {
	            	  wanted.add(file);
	              }
	              return FileVisitResult.CONTINUE;
	           }

	           public FileVisitResult visitFileFailed(Path file, IOException e) {
	        	   return FileVisitResult.CONTINUE;
	           }
	        });
	   }
	   
	   /*
	    * This method will set the directory of this PDFScrapper to the Path given to it.
	    * input: a Path type. The Path you would like to set as the directory to have this object scrape.
	    * output: confirmation when set.
	    */
	   public void setDir(Path dir){
		   this.directory = dir;
		   System.out.println("Directory is set to " + dir.toString() + " successfully.");
	   }
	   
	   /*
	    * This method will remove the inputed id from the wanted list.
	    * input: the String object of the id that you want to remove from the list of docs you want.
	    * output: confirmation on success or fail.
	    */
	   public void removeFromWanted(Path p){
		   if(wanted.remove(p)){
			   System.out.println(identifier(p) + " is removed from the wanted list.");
		   } else {
			   System.out.println("Could not find " + identifier(p) + " in the wanted list. It is not removed.");
		   }
	   }
	   
	   /*
	    * This method should take out everything before the introduction and abstract.
	    * input: a String representing the body you want to clean.
	    * output: a String representing the cleaned body of the text.
	    */
	   public String cleanBody(String body){
		   int index = 0;
		   if (body.indexOf("INTRODUCTION") != -1){
   				index = (body.indexOf("INTRODUCTION")+12);
   			}
   			else if (body.indexOf("Introduction") != -1){
   				index = (body.indexOf("Introduction")+12);
   			}
   			else if (body.indexOf("ABSTRACT") != -1){
   				index = (body.indexOf("ABSTRACT")+8);
   			}
   			else if (body.indexOf("Abstract") != -1){
   				index = (body.indexOf("Abstract")+8);
   			}
   			else{
   				int count = 0;
   				for (int i = -1; (i = body.indexOf("\r\n", i + 1)) != -1; ) {
   					count++;
   				    if (count == 19){
   				    	index = i;
   				    	break;
   				    }
   				}
   				if (count > 19) {
   					index = 280;
   				}
   			}
   			String returnVal body.substring(index);
   			returnVal = returnVal.replaceAll("-\r\n", "").replaceAll("\r\n", " ").replaceAll("\n", "");
   			return returnVal;
	   }
	   
	   /*
	    * This method will change the given id into its path.
	    * input: the String of the id you would like the filename/Path object to.
	    * output: the Path that is linked to the id.
	    */
	   public Path toFilePath(String id){
		   String temp = id.substring(0, id.length()-8)+ id.substring(id.length()-7);
		   temp = temp.replace("oai:arXiv.org:", "") + ".pdf";
		   temp = directory.toString() + "\\" + temp;
		   return Paths.get(temp).toAbsolutePath();
	   }
	   
	   /*
	    * This method will print out the whole list of paths currently in the wanted HashSet.
	    */
	   public void printWanted(){
		   for(Path p : wanted){
			   System.out.println(p.toString());
		   }
	   }
	   
	   /*
	    * Given the Path of the file and the body as a String, this method will write the 
	    */
		public void write(Path file, String body){
			Writer writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writepath + file.getFileName().toString().replace(".pdf", ".txt")), "utf-8"));
				writer.write(body);
				System.out.println("File successfully written to " + writepath + file.getFileName().toString().replace(".pdf", ".txt"));
			} catch (IOException e) {
				System.out.println("IOException.");
			} finally {
				try {writer.close();} catch (Exception e) {}
			}
		}
		
		/*
		 * This method will set the write path.
		 * 
		 * input: the String of the directory you want to write txt files in. (i.e. "D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\")
		 */
		public void setWritePath(String wpath){
			this.writepath = wpath;
		}
}
