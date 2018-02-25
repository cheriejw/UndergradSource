import java.io.BufferedWriter;
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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class JSONBuilder {
	public static void main(String[]args) throws ParserConfigurationException, SAXException, IOException{
		//Variables
		DataMerger merge = new DataMerger();
		Path temporary = Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\");
		Path xmlDir = Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\XMLfiles"); //better visual
		merge.setTextDir(temporary.toString());
		
		//JSON Variables
		Gson gson = new Gson();
//		JsonParser parser = new JsonParser();
//        JsonElement element;
        
		
		Files.walkFileTree(xmlDir, new SimpleFileVisitor<Path>() {
			//Walk through XML
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				if (xmlDir.equals(dir)) {
					return FileVisitResult.CONTINUE;
				}
				return FileVisitResult.SKIP_SUBTREE;
			}

			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				
				//What you want to do here!
				HashSet<JSObjectN> res = null;
				
				try {
					res = merge.merge(file);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (res == null){ //Java being Java
					res = new HashSet<JSObjectN>();
				}
				
				for (JSObjectN resu : res){
					String json = gson.toJson(resu);
					write(merge.toFilePath(resu.identifier), json);
				}
				//Turn res into JSON and write to file.
				return FileVisitResult.CONTINUE;
			}
			
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				return FileVisitResult.CONTINUE;
			}
		});
		
		System.out.println("Files from " + xmlDir.toString() +" turned to JSON.");

	}
	
	public static void write(Path identifier, String json){
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("D:\\Workspaces\\CECS429\\Milestone1\\JsonDocs\\" + identifier.getFileName().toString().replace(".txt", ".json")), "utf-8"));
			writer.write(json);
		} catch (IOException e) {
			System.out.println("IOException.");
		} finally {
			try {writer.close();} catch (Exception e) {}
		}
	}
	
	public static void print(HashSet<JSObjectN> res){
		for (JSObjectN resu : res){
			System.out.println(resu.identifier);
			System.out.println(resu.title);
			for (String aName : resu.author){
				System.out.println(aName);
			}
			System.out.println(resu.body);
		}
	}
}
