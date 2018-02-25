import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*
 * This class will have methods associated or necessary to parse the XML.
 * 
 * Reads one XML file that contains multiple of the datas.
 * 
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
 */

public class XMLParser {

	//Private variables
	private File xmlFile; //location of the xmlFile
	private HashMap<String, Integer> ids; //all the identifiers in this XMLFile
	
	//Private variables for the XML parsing.
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private NodeList nList;
	private Node nNode;
	
	XMLParser(){
		
	}
	
	/*
	 * This method will set the file you want to parse.
	 * This also initializes a few other private methods.
	 * input: The String of the xml path you want to make a new File object for.
	 */
	public void setFile(String xml) throws ParserConfigurationException, SAXException, IOException{
		xmlFile = new File(xml);
		System.out.println("File set to " + xml);
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
	} 
	
	/*
	 * Each XML file has a different number of "record" nodes.
	 * This method will return the current number of "record" tags in this XML file.
	 */
	public int getNodeMax(){
		nList = doc.getElementsByTagName("ListRecords");
		nNode = nList.item(0);
		Element eElement = (Element) nNode;
		return eElement.getElementsByTagName("record").getLength();
	}
	
	/*
	 * This method will go to the record and get the tag, with exception of authors.
	 * 
	 * Uses JSObjectN to store values and sends back, uses an externally iterating nodeNumber
	 * External iteration should start at 0.
	 */
	public JSObjectN parse(int nodeNumber){
		//Initialization
	    HashSet<String> authors = new HashSet<String>();  
	    JSObjectN object = new JSObjectN();
	    
		try {	
	          nList = doc.getElementsByTagName("ListRecords"); //They will take this tag from any level. "record"
	          for (int temp = 0; temp < nList.getLength(); temp++) { //Iterates through nList nodes.
	             nNode = nList.item(temp);
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                Element eElement = (Element) nNode;
	                
	                Node subNode = eElement.getElementsByTagName("record").item(nodeNumber);  
	                Element subElement = (Element) subNode; //subElements are the child nodes of "record" tag.
	                
	                object.identifier = subElement.getElementsByTagName("identifier").item(0).getTextContent();
	                object.date = subElement.getElementsByTagName("created").item(0).getTextContent();
	                object.abstrac = subElement.getElementsByTagName("abstract").item(0).getTextContent();
	                object.title = subElement.getElementsByTagName("title").item(0).getTextContent();

	                //author //Element.getElementsByTagName(name) returns a NodeList
	                Node authorNode = subElement.getElementsByTagName("authors").item(0);  
	                Element authorElement = (Element) authorNode;
	                Node temporary;
	                Element eTemp;
	                
	                for (int i = 0; i < authorElement.getElementsByTagName("author").getLength(); i++){
	                	temporary = authorElement.getElementsByTagName("author").item(i);	
	                	eTemp = (Element)temporary;
	                	authors.add(eTemp.getElementsByTagName("forenames").item(0).getTextContent() + " " 
	                			+ eTemp.getElementsByTagName("keyname").item(0).getTextContent());
	                }
	                
	                object.author = authors;
	                //At this point the only object empty field is body.
	                
	             }
	          }
	       } catch (Exception e) {
	          e.printStackTrace();
	       }
		return object;
	}
	
	/*
	 * sets the identifier:position
	 * Hopefully that corresponds to the node number :C
	 */
	public void getIds() throws IOException, SAXException, ParserConfigurationException{
		ids = new HashMap<String, Integer>();
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		Document docm = null;
		
		if (docm == null){
			try {
				docm = dBuilder.parse(xmlFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Element element = docm.getDocumentElement();
		NodeList list = element.getElementsByTagName("identifier");
		
		for (int i = 0; i < list.getLength(); i++){
        	if (list != null && list.getLength() > 0) {
        		NodeList subList = list.item(i).getChildNodes();
                if (subList != null && subList.getLength() > 0) {
                	//System.out.println(subList.item(0).getNodeValue());
                	//I am only doing these files...
                	if (subList.item(0).getNodeValue().contains("oai:arXiv.org:cs/00") ||
                			subList.item(0).getNodeValue().contains("oai:arXiv.org:cond-mat/00") ||
                			subList.item(0).getNodeValue().contains("oai:arXiv.org:astro-ph/00")){
                		ids.put(subList.item(0).getNodeValue(), i);
                		System.out.println("FOUND: " + subList.item(0).getNodeValue() + "\n");
                	}
                }
            }
        }
		System.out.println("Indexed relevant values of " + xmlFile.toString());
	}
	
	public HashMap<String, Integer> getMap(){
		return ids;
	}

}
