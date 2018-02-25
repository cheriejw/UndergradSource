import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*
 * Run the PDFScrapper first then set the directory where you placed the output files.
 * 
 * Sounds better than DataCombiner.
 * Merges the necessary information into a JSObjectN Object that represents a JSON file.
 * Object is created, filled, and returned in the merge method.
 * Manages the XMLParser and PDFScrapper
 * 
 */

public class DataMerger {
	
	private Path textDir; //The location of the txt files that contains the body.
	private HashSet<String> identifiers;
	private HashSet<JSObjectN> valid;
	
	//Constructor
	public DataMerger(){
		
	}
	
	/*
	 * Overloaded Constructor.
	 * input: The String of the URI to set as Path of the text file for Body.
	 */
	public DataMerger(String txtL){
		setTextDir(txtL);
	}
	
	/*
	 * Given an identifier, this method will create, fill and return an JSObjectN.
	 */
	private JSObjectN addBody(String id, JSObjectN xmlAdded) throws IOException{
		//Filling the JSObjectN.
		xmlAdded.body = getBody(toFilePath(id));
		
		//To get XML things change the XMLfiles.
		
		//Returning the filled JSObjectN.
		return xmlAdded;
	}
	
	/*
	 * Give this the XML file that you want to strip the identifiers from the hash.
	 * The identifiers from the hash are the identifiers that you have contained in
	 * the text directory you gave in.
	 * 
	 */
	public HashSet<JSObjectN> merge(Path allXml) throws ParserConfigurationException, SAXException, IOException{
		//Initializations
		XMLParser parse = new XMLParser();
		int max;
		JSObjectN obj = new JSObjectN();
		DataMerger merge = new DataMerger();
		HashSet<String> hash;
		valid = new HashSet<JSObjectN>();
		
		parse.setFile(allXml.toString());
		parse.getIds();
		HashMap<String, Integer> map = parse.getMap();
		
		merge.setTextDir("D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\");
		merge.hashId();
		hash = merge.getIds();
		
		if(map.isEmpty()){
			System.out.println("Map is empty. :,(");
		} else {
			max = parse.getNodeMax(); //adds body and saves in valid.
			for (int i = 0; i < max; i++){ //Walking through nodes in the XML file
				obj = parse.parse(i);
				if(hash.contains(obj.identifier)){ //Only want the ones you have
					JSObjectN adding = addBody(obj.identifier, obj);
					valid.add(adding); //added the JSON full object to valid. 
				} 
			}
		}
		System.out.println(allXml.toString() + " is merged and the JSON objects are returned.");
		return valid; //the return value are the wanted ids in the ONE xml file.
	}
	
	/*
	 * This is a mutator method that will set the TextLoc Private Variable.
	 * input: The String of the text file Location. i.e. D:\Workspaces\CECS429\Milestone1\jDom
	 */
	public void setTextDir(String txtL){
		this.textDir = Paths.get(txtL);
	}
	
	/*
	 * This method will get the String from the text file.
	 * The CharSet encoding is already set to StandardCharsets.UTF_8.
	 * 
	 * input: the Path to the file you want to get the String of.
	 */
	public String getBody(Path path) throws IOException{
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	/*
	 * This method will change the given id into its path.
	 * input: the String of the id you would like the filename/Path object to
	 * 	along with the String of the directory I would place the file in.
	 * output: the Path that is linked to the id. Path will be the original pdf location.
	 */
	public Path toFilePath(String id){
		String temp = id.substring(0, id.length()-8)+ id.substring(id.length()-7);
		temp = temp.replace("oai:arXiv.org:", "") + ".txt";
		temp = textDir.toString() + "\\" + temp;
		return Paths.get(temp).toAbsolutePath();
	}
	
	/*
	 * This method will go through the text files I will use and save the identifiers
	 * in a HashSet.
	 */
	public void hashId() throws IOException{
		identifiers = new HashSet<String>();
		
		Files.walkFileTree(textDir, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				if (textDir.equals(dir)) {
					return FileVisitResult.CONTINUE;
				}	
				return FileVisitResult.SKIP_SUBTREE;
			}
			
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (file.toString().endsWith(".txt")) {
					identifiers.add(identifier(file));
				}
				return FileVisitResult.CONTINUE;
			}
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/*
	 * This method will give you the identifier String.
	 * input: the Path of the file or document you want the id for.
	 * output: the identifier obtained from the string.
	 */
	public String identifier(Path file){
		String temp = ("oai:arXiv.org:" + file.getFileName().toString().replace(".txt", ""));//replace("000", "/000")
		return temp.substring(0, temp.length()-7)+ "/" + temp.substring(temp.length()-7);
	}
	
	public HashSet<String> getIds(){
		return identifiers;
	}
	
}
