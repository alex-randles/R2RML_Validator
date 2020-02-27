package JavaClasses;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class Domain {


    public static boolean validateDomain(String mappingSubject, String mappingPredicate) {
        try {
            System.out.println("CHECKING DOMAIN");
            Model data = ModelFactory.createDefaultModel();
            data.read(mappingSubject);
            boolean inDomain = SPARQL.askQuery(mappingSubject, String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingPredicate, mappingSubject));
            String s = String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingPredicate, mappingSubject);
            System.out.println(s + inDomain);
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
