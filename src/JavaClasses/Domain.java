package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import java.util.ArrayList;
import java.util.List;


public class Domain {

    public static boolean validateDomain(String predicateURI) {
        try {
            String checkDomainQuery = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "SELECT ?domain {<%s> rdfs:domain ?domain }", predicateURI);
            ResultSet result = SPARQL.selectQuery(predicateURI, checkDomainQuery);
            String domain = SPARQL.getStringVariable(result, "?domain");
            if (domain.isEmpty()){
                return true;
            }
            boolean inDomain = SPARQL.askQuery(FileNames.originalMappingFile, String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                    "ASK {?s rr:class <%s> }", domain));
            if (!inDomain) {
                Refinement.findValidDomain(predicateURI);
            }
            return inDomain;

        } catch (Exception e) {
            Refinement.findValidDomain(predicateURI);
            return true;
        }
    }

}
