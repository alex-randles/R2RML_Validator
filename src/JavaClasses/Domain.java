package JavaClasses;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.List;


public class Domain {

    public static void main(String[] args) {
        System.out.println(validateDomain("http://dbpedia.org/ontology/club"));
    }

    public static boolean validateDomain(String mappingPredicate) {
        try {
            String checkDomainQuery = String.format("SELECT ?domain {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", mappingPredicate);
            ResultSet result = SPARQL.selectQuery(mappingPredicate, checkDomainQuery);
            String domain = SPARQL.getStringVariable(result, "?domain");
            boolean inDomain = SPARQL.askQuery(FileNames.originalMappingFile, String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                    "ASK {?s rr:class <%s> }", domain));
            System.out.println("domain validation");
            System.out.println(inDomain);
            System.out.println(String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                    "ASK {?s rr:class <%s> }", domain));
            if (!inDomain) {
                Refinement.findValidDomain(mappingPredicate);
            }
            return inDomain;

        } catch (Exception e) {
            Refinement.findValidDomain(mappingPredicate);
            System.out.println(e + " ERROR ");
            return true;
        }
    }

}
