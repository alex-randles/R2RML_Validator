package JavaClasses;

import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.*;
import java.io.File;
import java.io.FileOutputStream;

public class SPARQL {

    public static void main(String[] args){

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
            // System.out.println("ASK QUERY ERROR " + e);
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
            Model model = ModelFactory.createDefaultModel().read(URI);
            Query query = QueryFactory.create(queryString) ;
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                qexec.close() ;
                return results ;
            }


        }
        catch (Exception e){
            System.out.println("SELECT query error "  + e);
            return null;
        }
    }

    public static String getStringVariable(ResultSet results, String variableName){
        try {
            for(;results.hasNext();){
                QuerySolution soln = results.nextSolution();
                System.out.println(soln);
                String s = soln.get(variableName).toString();
                return s;
            }
            return "";
        }
        catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }
}
