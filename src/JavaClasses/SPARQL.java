package JavaClasses;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

import java.io.File;
import java.io.FileOutputStream;

public class SPARQL {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#" ;
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";


    public static void main(String[] args){

        String query = String.format("ASK {<%s> <%s> ?label }",FOAF_NS+"age", RDFS_NS+"label");
        System.out.println(SPARQL.askQuery( FOAF_NS+"Person", query));

//        String updateQuery = "INSERT {?objectMapObject <http://www.w3.org/ns/r2rml#termType> 'testing'} WHERE {\n" +
//                "  ?predicateSubject ?predicate <http://xmlns.com/foaf/0.1/knows>.\n" +
//                "  ?predicateSubject <http://www.w3.org/ns/r2rml#objectMap> ?objectMapObject.\n" +
//                "  ?objectMapObject \t<http://www.w3.org/ns/r2rml#termType> ?termTypeObject.          \n" +
//                "}";
//        System.out.println(updateQuery);

    }


    public static boolean askQuery(String URI, String query){
        try {
            Model model = ModelFactory.createDefaultModel().read(URI);
            Query askQuery = QueryFactory.create(String.format(query));
            QueryExecution qexec = QueryExecutionFactory.create(askQuery, model) ;
            boolean result = qexec.execAsk() ;
            qexec.close() ;
            return result;
        }
        catch(Exception e){
            System.out.println("ASK QUERY ERROR " + e);
            return false;
        }


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

    public static ResultSet selectQuery(String URI, String queryString){
        try{
            Model model = ModelFactory.createDefaultModel() ;
            model.read(URI);
            Query query = QueryFactory.create(queryString) ;
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                return results ;    // Passes the result set out of the try-resources
            }

        }
        catch (Exception e){
            System.out.println("SELECT query error "  + e);
            return null;
        }



    }
}
