package JavaClasses;

import java.net.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;

public class DereferenceURI {

    public static void main(String[] args){
    }

    public static boolean getResponseCode(String string_URL) {
        try {
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(string_URL + " produces error code " + code);
            return code == 200;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

   public static boolean accessRDF(String uri){
        try {
            Model data = ModelFactory.createDefaultModel();
            data.read(uri);
            StmtIterator iter = data.listStatements();
            return iter.hasNext();
        }
        catch(Exception e){
             System.out.println("Error fetching RDF " + e);
             return false;
             }
        }

}