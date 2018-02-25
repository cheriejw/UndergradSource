import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*
 * Like the class name denotes, this class will clean the directory specified in the pathFile Path.
 * It will remove all of the XML that do not meet the conditions specified in the XMLParser.
 */

public class CleanDirToFilterSetInXMLParser {
	public static void main(String[]args) throws IOException{
		
		XMLParser parse = new XMLParser();
		Path pathFile = Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\XMLfiles\\");
		
		Files.walkFileTree(pathFile, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				if (pathFile.equals(dir)) {
					return FileVisitResult.CONTINUE;
				}	
				return FileVisitResult.SKIP_SUBTREE;
			}
			
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (file.toString().endsWith(".xml")) {
					try {
						parse.setFile(file.toString());
						parse.getIds();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//more...
					HashMap<String, Integer> map = parse.getMap();
					if(map.isEmpty()){
						System.out.println("Map is empty. :,(");
				    	try {
				    	    Files.delete(file);
				    	    System.out.println(pathFile.toString() + " deleted.");
				    	} catch (NoSuchFileException x) {
				    	    System.err.format("%s: no such" + " file or directory%n", pathFile);
				    	} catch (DirectoryNotEmptyException x) {
				    	    System.err.format("%s not empty%n", pathFile);
				    	} catch (IOException x) {
				    	    // File permission problems are caught here.
				    	    System.err.println(x);
				    	}
					}
				}
				return FileVisitResult.CONTINUE;
			}
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
