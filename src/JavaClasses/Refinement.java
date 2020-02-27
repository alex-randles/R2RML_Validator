package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class Refinement {

    public static void findValidDomain(String URI) {

        System.out.println("FINDING DOMAIN");
        String queryString = String.format("SELECT ?domain WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", URI);
        ResultSet results = SPARQL.selectQuery(URI, queryString);
        for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String domain = soln.get("?domain").toString() ;   // Get a result variable - must be a literal
            if(!domain.isEmpty()){
                AddDomain.AddDomainTriple(domain, "./resources/sample_map.ttl");
                System.out.println("ADDING DOMAIN " + domain);
                return;

            }

        }

    }
}
