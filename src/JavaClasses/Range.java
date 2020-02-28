package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class Range {

    public static boolean validateRange(String predicateURI, String dataTypeURI){
        try{
            String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range} ", predicateURI);
            ResultSet results = SPARQL.selectQuery(predicateURI, query);
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                String range = soln.get("?range").toString();   // Get a result variable - must be a literal
                if (!range.isEmpty()) {
                    String[] parts = range.split("#");
                    if (parts[0].equals("http://www.w3.org/2001/XMLSchema")) {
                        if(!range.equals(dataTypeURI)){
                            return false;
                        }

                    }
                }
            }
            return true;
        }
        catch(Exception e){
            System.out.println(e + "validating range");
        }
        return true;
    }
}
