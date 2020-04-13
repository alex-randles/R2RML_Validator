package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.*;
import java.io.File;
import java.io.FileOutputStream;

public class SPARQL {

    public static void main(String[] args){
        String s = "SELECT ?o WHERE {<http://xmlns.com/foaf/0.1/mbox> <http://www.w3.org/2000/01/rdf-schema#domain> ?o}";
        ResultSet results = selectQuery(FOAF.NS, s);
        System.out.println(SPARQL.getStringVariable(results, "?o"));
    }

    public static boolean askQuery(String URI, String queryString){
        try {
//            Model model = ModelFactory.createDefaultModel().read(URI);
//            Query askQuery = QueryFactory.create(String.format(query));
//            QueryExecution qexec = QueryExecutionFactory.create(askQuery, model) ;
//            boolean result = qexec.execAsk() ;
//            qexec.close() ;
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel().read(URI));
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
            Model model =  ModelFactory.createDefaultModel().read(URI) ;
            try (QueryExecution qexec = QueryExecutionFactory.create(queryString, model)) {
                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                qexec.close();
                return results ;    // Passes the result set out of the try-resources
            }

//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel().read(URI));
//            ResultSet results = qexec.execSelect();
//            results = ResultSetFactory.copyResults(results) ;
//            qexec.close();
//            return results;
//             Model model = ModelFactory.createDefaultModel().read(URI);
//             Query query = QueryFactory.create(URI, queryString) ;
//
//            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
//                ResultSet results = qexec.execSelect() ;
//                results = ResultSetFactory.copyResults(results) ;
//                qexec.close() ;
//                return results ;
//            }
//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexec = QueryExecutionFactory.sparqlService(URI, query);
//            ResultSet results = qexec.execSelect();
//            return results;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static String getStringVariable(ResultSet results, String variableName){
        try {
            String stringVariable = results.nextSolution().get(variableName).toString();
            return stringVariable;
//            for(;results.hasNext();){
//                QuerySolution soln = results.nextSolution();
//                String stringVariable = results.nextSolution().get(variableName).toString();
//                return stringVariable;
//            }
//            return "";
        }
        catch (Exception e) {
            return "";
        }
    }
}
