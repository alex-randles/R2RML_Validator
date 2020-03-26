package JavaClasses;


import org.apache.jena.query.ResultSet;

import java.util.zip.Deflater;

public class Domain {


    public static boolean validateAllDomains(String[] mappingSubject, String mappingPredicate){
        // if can not retrieve vocabulary, return true
        try {
            String checkDomainQuery = String.format("SELECT ?domain {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", mappingPredicate);
            ResultSet result = SPARQL.selectQuery(mappingPredicate, checkDomainQuery);
            String domain = SPARQL.getStringVariable(result, "?domain");
            if(domain.isEmpty()){
                return true;
            }
            if (mappingSubject.length == 0 ) {
                System.out.println("NO DOMAIN CLASSES");
                Refinement.findValidDomain(mappingPredicate);
                return true;
            }
            for (String subject : mappingSubject) {
                if (validateDomain((subject), mappingPredicate)) {
                    return true;
                }
            }
            return false;
        }
        catch(Exception e){
            return true;
        }

    }

    public static boolean validateDomain(String mappingSubject, String mappingPredicate) {
        try {

            System.out.println("CHECKING DOMAIN");
            boolean inDomain = SPARQL.askQuery(mappingSubject, String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingPredicate, mappingSubject));
            // String s = String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingPredicate, mappingSubject);
            System.out.println("RESULT OF CHECKING DOMAIN");
            System.out.println(inDomain);
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
