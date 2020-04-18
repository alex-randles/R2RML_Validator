package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SPARQL {

    public static void main(String[] args){
        String s = "SELECT ?o WHERE {<http://xmlns.com/foaf/0.1/mbox> <http://www.w3.org/2000/01/rdf-schema#domain> ?o}";
        ResultSet results = selectQuery(FOAF.NS + "name", s);
        System.out.println(SPARQL.getStringVariable(results, "?o"));
        //     saveRDFLocally(NS.FOAF_NS + "age");
    }

    public static boolean askQuery(String URI, String queryString){
        try {
            String localFileName = saveRDFLocally(URI);
            Model model =  ModelFactory.createDefaultModel().read(localFileName) ;
//            Model model =  ModelFactory.createDefaultModel().read(URI) ;
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            boolean result = qexec.execAsk();
            qexec.close();
            return result;
        }
        catch(Exception e){
            return false;
        }


    }
    public static void updateData(String updateQuery, String inputFile, String outputFile){
        Model model = ModelFactory.createDefaultModel().read(inputFile);
        UpdateAction.parseExecute(updateQuery, model);
        File file = new File(outputFile);
        try{
            FileOutputStream fop = new FileOutputStream(file);
            model.write(fop, "TURTLE");
            fop.flush();
            fop.close();
        }
        catch(Exception e) {
        }

    }

    public static ResultSet selectQuery(String URI, String queryString){
        try{
            String localFileName = saveRDFLocally(URI);
            Model model =  ModelFactory.createDefaultModel().read(localFileName) ;
            try (QueryExecution qexec = QueryExecutionFactory.create(queryString, model)) {
                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                qexec.close();
                return results ;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public static String saveRDFLocally(String URI) throws IOException {
        String localFileName = "./cache/"  + URI.replaceAll("[^\\w]", "_") + ".ttl";
        File localFile = new File(localFileName);
        if (localFile.createNewFile() || localFile.length() == 0 || !URI.startsWith("http")){
            try {
                Model data = ModelFactory.createDefaultModel().read(URI);
                FileOutputStream localFileStream = new FileOutputStream(localFile);
                data.write(localFileStream, "TURTLE");
                localFileStream.flush();
                localFileStream.close();

            } catch (Exception e) {
            }
            return localFileName;
        }
        else{
            return localFileName;
        }
    }

    public static String getStringVariable(ResultSet results, String variableName){
        try {
            String stringVariable = results.nextSolution().get(variableName).toString();
            return stringVariable;
        }
        catch (Exception e) {
            return "";
        }
    }
}