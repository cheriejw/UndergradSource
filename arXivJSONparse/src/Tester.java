import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

/*
 * Testing functionalities of Objects.
 */

public class Tester {
	public static void main(String[]args) throws IOException, ParserConfigurationException, SAXException, JDOMException{
		//Testing PDFScrapper
		
		//Instantiations
		Scanner pause = new Scanner(System.in);
		PDFScrapper scrape = new PDFScrapper();
		scrape.setDir(Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\PDFandRaw\\Sample").toAbsolutePath());
		scrape.setWritePath("D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\");
		//DataMerger merger = new DataMerger("D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\");
		
		//Testing the methods...
//		scrape.logWanted();
//		scrape.printWanted();
//		scrape.removeFromWanted(Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\PDFandRaw\\Sample\\hep-ex0006035.pdf").toAbsolutePath());
//		scrape.convert();
		
		//Testing XMLParser
		
		//Instantiations
//		XMLParser parse = new XMLParser();
//		int max;
//		JSObjectN obj = new JSObjectN();
		DataMerger merge = new DataMerger();
//		HashSet<String> hash;
		//String testXML = "<test>more tag<tag>hello</tag></test>";
		
		//Testing the methods...
		Path allXml = Paths.get("D:\\Workspaces\\CECS429\\Milestone1\\XMLfiles\\2009-09-30-00000005.xml"); //better visual
		
//		parse.setFile(allXml.toString());
//		parse.getIds();
//		HashMap<String, Integer> map = parse.getMap();
//		
		//You have to do this.
		merge.setTextDir("D:\\Workspaces\\CECS429\\Milestone1\\TextOfPDF\\");
//		merge.hashId();
//		hash = merge.getIds();
//		
//		if(map.isEmpty()){
//			System.out.println("Map is empty. :,(");
//		}
//		
//		max = parse.getNodeMax();
//		for (int i = 0; i < max; i++){ //walking through the nodes in the one xml file.
//			//Time doesn't matter
//			obj = parse.parse(i);
//			if(hash.contains(obj.identifier)){ //collecting the valid objects.
//				//I want this object
//				System.out.println(obj.identifier);
//				System.out.println(obj.title);
//				for (String aName : obj.author){
//					System.out.println(aName);
//				}
//			}
//		}
//		
		HashSet<JSObjectN> res = merge.merge(allXml);
		
		//Check the items
		for (JSObjectN resu : res){
			System.out.println(resu.identifier);
			System.out.println(resu.title);
			for (String aName : resu.author){
				System.out.println(aName);
			}
			System.out.println(resu.body);
		}
		//Items Check!
		
		//System.out.println(parse.getNodeMax());
		pause.close();
	}
}