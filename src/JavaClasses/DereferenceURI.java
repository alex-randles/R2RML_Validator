package JavaClasses;

import org.apache.jena.rdf.model.*;
import java.net.*;

public class DereferenceURI {

    public static boolean getResponseCode(String string_URL) {
        try {
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            return code == 200;
        }
        catch (Exception e) {
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
             return false;
             }
        }

}