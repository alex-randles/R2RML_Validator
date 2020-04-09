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

    public static boolean validateDomain(String predicateURI) {
        try {
            String checkDomainQuery = String.format("SELECT ?domain {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", predicateURI);
            ResultSet result = SPARQL.selectQuery(predicateURI, checkDomainQuery);
            String domain = SPARQL.getStringVariable(result, "?domain");
            boolean inDomain = SPARQL.askQuery(FileNames.originalMappingFile, String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                    "ASK {?s rr:class <%s> }", domain));
            System.out.println("domain validation");
            System.out.println(inDomain);
            System.out.println(predicateURI);
            if (!inDomain) {
                Refinement.findValidDomain(predicateURI);
            }
            return inDomain;

        } catch (Exception e) {
            Refinement.findValidDomain(predicateURI);
            System.out.println(e + " ERROR ");
            return true;
        }
    }

}
