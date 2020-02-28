package JavaClasses;


import java.util.zip.Deflater;

public class Domain {


    public static boolean validateAllDomains(String[] mappingSubject, String mappingPredicate){
        if (!DereferenceURI.accessRDF(mappingPredicate)){
            return false;
        }
        if (mappingSubject.length == 0){
            System.out.println("NO DOMAIN CLASSES");
            Refinement.findValidDomain(mappingPredicate);
            return true;
        }
        for (String subject : mappingSubject){
            if (validateDomain((subject), mappingPredicate)){
                return true;
            }
        }
        return false;

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
