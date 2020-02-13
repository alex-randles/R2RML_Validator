package JavaClasses;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.*;

import java.io.File;
import java.io.FileOutputStream;

public class SPARQL {
    public static void main(String[] args){
        String updateQuery = "INSERT {?objectMapObject <http://www.w3.org/ns/r2rml#termType> 'testing'} WHERE {\n" +
                "  ?predicateSubject ?predicate <http://xmlns.com/foaf/0.1/knows>.\n" +
                "  ?predicateSubject <http://www.w3.org/ns/r2rml#objectMap> ?objectMapObject.\n" +
                "  ?objectMapObject \t<http://www.w3.org/ns/r2rml#termType> ?termTypeObject.          \n" +
                "}";
        System.out.println(updateQuery);

    }
    public static void updateData(String updateQuery, String inputFile, String outputFile){
        System.out.println("UPDATE QUERY " + updateQuery);
        Model model = ModelFactory.createDefaultModel();
        model.read(inputFile);
        UpdateAction.parseExecute(updateQuery, model );
        model.write( System.out, "TURTLE" );
        File file = new File(outputFile);
        try{

            FileOutputStream fop = new FileOutputStream(file);
            // fop.write(contentInBytes);
            model.write(fop, "TURTLE");
            fop.flush();
            fop.close();
            String result = "UPDATE QUERY written to " + outputFile;
            System.out.println(result);


        }

        catch(Exception e) {
            String result = "File not found!";
            System.out.println(result);
        }

    }
}
