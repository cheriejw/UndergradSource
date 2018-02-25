

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParserDemo {
   public static void main(String[] args){

      try {	
         File inputFile = new File("D:\\Workspaces\\CECS429\\Milestone1\\XMLfiles\\unzipedXML\\2007-05-31-00000000.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         
         System.out.println("Root element :" + doc.getDocumentElement().getTagName());
         NodeList nList = doc.getElementsByTagName("ListRecords"); //They will take this tag from any level.
         System.out.println("----------------------------");
         
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp); //If you have many "ListRecords" it is going through the "ListRecords"
            System.out.println("\nCurrent Element :" 
               + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	
               Element eElement = (Element) nNode; //Element type and Document type both have getElementsByTagName
               //getElementsByTagName returns a NodeList
               //NodeList has an item method that returns a Node, whose getNodeName is the TagName
               //Node.ELEMENT_NODE returns 1 and nNode.getNodeType() returns 1 too.
               
               Node nidalee = eElement.getElementsByTagName("record").item(0);
               Element nidalee1 = (Element) nidalee;
               System.out.println(nidalee1.getElementsByTagName("identifier").item(0).getTextContent());
               //ElementsByTagName works on all levels.
               
               System.out.println("record : " 
                  + eElement
                  .getElementsByTagName("record") //This is a NodeList
                  .item(0)//If there are more than one record it will show up here.
                  //I have to type cast it to (Element) to get access to the .getElementsByTagName method.
                  .getTextContent());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}