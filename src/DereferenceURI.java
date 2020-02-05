package JavaClasses;

import java.net.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import  	org.xml.sax.SAXException ;
import javax.xml.stream.*;
import org.apache.jena.rdf;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ValidationUtil;
import java.io.*;

public class DereferenceURI{

    public static void main(String[] args){
        // main method for testing purposes
       getRDF("http://xmlns.com/foaf/0.1/Agent");
    }

    public static boolean getResponseCode(String string_URL){
        try{
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(string_URL + " produces error code " + code);
            if (code == 200) {
                return true;
            }
            return false;
        }
        catch(Exception e){
            System.out.println(e);
            return true ;
        }

}
    public static String getRDF(String uri){
        try{

        		Model data = ModelFactory.createDefaultModel();
		data.read("http://xmlns.com/foaf/0.1/Agent",
           "RDF/XML");
//             URL url = new URL(string_URL);
//             HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//             connection.setRequestMethod("GET");
//             // connection.setRequestProperty("Accept", "application/xml");
//             connection.connect();
//             int code = connection.getResponseCode();
//             System.out.println(string_URL + " produces error code " + code);
//             return null;
//             BufferedReader in = new BufferedReader(
//             new InputStreamReader(connection.getInputStream()));
//             String inputLine;
//             StringBuffer content = new StringBuffer();
//             while ((inputLine = in.readLine()) != null) {
//                      content.append(inputLine);
//             }
//             in.close();
//             System.out.print(content.toString());
//             return content.toString();
       //String uri =
    // "http://api.flurry.com/eventMetrics/Event?apiAccessCode=?????&apiKey=??????&startDate=2011-2-28&endDate=2011-3-1&eventName=Tip%20Calculated";

URL url = new URL(uri);
HttpURLConnection connection =
    (HttpURLConnection) url.openConnection();
connection.setRequestMethod("GET");
// connection.setRequestProperty("Accept", "application/xml");
connection.setRequestProperty("Accept", "application/rdf+xml");
int code = connection.getResponseCode();

// System.out.println(uri + " produces error code " + code);
// InputStream xml = connection.getInputStream();
// System.out.println(connection.getInputStream());
// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
// DocumentBuilder db = dbf.newDocumentBuilder();
// Document doc = db.parse(xml);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                     content.append(inputLine);
            }
            in.close();
            // System.out.print(content.toString());
           // whenWriteStringUsingBufferedWritter_thenCorrect(content.toString());

writeFile( content);
parseXml();
            // return content.toString();

        }
        catch(Exception e){
            System.out.println(e + "3474374747");
        }
        return "";
        }


	public static void writeFile(StringBuffer sbf) throws IOException {


		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("foaf.xml")));

		//write contents of StringBuffer to a file
		bwr.write(sbf.toString());

		//flush the stream
		bwr.flush();

		//close the stream
		bwr.close();

		System.out.println("Content of StringBuffer written to File.");
	}
//  </rdf:Property></rdf:RDF>
  public static void parseXml() {

      try {

	File fXmlFile = new File("foaf.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("rdfs:comment");

	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);

		System.out.println("\nCurrent Element :" + nNode.getNodeName());
		System.out.println(nNode);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			// System.out.println("Staff id : " + eElement.getAttribute("rdfs:domain"));
			// System.out.println("Nick Name : " + eElement.getElementsByTagName("rdf:Property").item(5));
			// System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }



}

