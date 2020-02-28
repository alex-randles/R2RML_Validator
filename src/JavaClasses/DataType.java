package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class DataType {

    public static void validateDataType(String URI, String mappingFile) {
        String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range}", URI);
        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String range = soln.get("?range").toString();   // Get a result variable - must be a literal
            if (!range.isEmpty()) {
                String[] parts = range.split("#");
                if (parts[0].equals("http://www.w3.org/2001/XMLSchema")) {
                    Refinement.AddDataTypeTriple(URI,range, mappingFile);
                    return;

                }
            }

        }
    }

}
