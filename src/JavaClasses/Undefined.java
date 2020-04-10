package JavaClasses;

import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

// method 1 - split URI and return name -> DONE
// method 2 - return true if two properties are similar - capitalize or one letter different -> DONE
// method 3 - return a similar class otherwise nothing
public class Undefined {

    public static String findSimilarDefinitions(String URI){
        Model model = ModelFactory.createDefaultModel().read(URI);
        String queryString = " select DISTINCT ?s  where {\n" +
                "  ?s ?p ?o\n" +
                "}";
        Query query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(queryString, model)) {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                String comparisionURI = soln.get("?s").toString();
                String definition = splitURI(comparisionURI);
                boolean comparisionResult = similarNames(definition, splitURI(URI));
                if (comparisionResult){
                    return comparisionURI;
                }
            }
        }
        return "";

    }
    public static boolean similarNames(String name1, String name2){
        name1 = name1.toLowerCase();
        name2 = name2.toLowerCase();
        if(name1.equals(name2)){
            return true;
        }
        int len = Math.min(name1.length(), name2.length());
        int differences = 0;
        for(int i = 0; i<len; i++){
                if(name1.charAt(i) != name2.charAt(i)){
                    differences+=1;
                }
        }
        differences += Math.max(name1.length(), name2.length()) - len;
        return differences <= 1;
    }

    public static String splitURI(String URI){
        try{
            String name = "";
            if(URI.contains("#")){
                String[] hashURI = URI.split("#");
                name = hashURI[hashURI.length-1];
            }
            else{
                String[] slashURI = URI.split("/");
                name = slashURI[slashURI.length-1];
            }
            return name;
        }
        catch(Exception e){
            return "";
        }
    }
}
