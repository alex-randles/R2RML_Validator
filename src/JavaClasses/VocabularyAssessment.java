package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.net.HttpURLConnection;
import java.net.URL;

public class VocabularyAssessment {


    public static void main(String[] args) {
        // System.out.println(test("http://purl.org/dc/terms/"));
//        System.out.println("human license : ");
//        System.out.println(hasHumanReadableLicense("<http://dbpedia.org/ontology/age>"));
//        System.out.print("machine license : ");
          System.out.println(test("http://dbpedia.org/ontology/"));
//        System.out.print("human labelling : ");
//        System.out.println(hasHumamReadableLabelling(NS.FOAF_NS+"name"));

    }

    public static boolean isAccessible(String URI){
        boolean result =  DereferenceURI.accessRDF(URI);
        return result;
    }

    public static boolean validateUndefined(String URI){
        if (!DereferenceURI.accessRDF(URI)){
            return false;
        }
        String query = String.format("ASK {<%s> ?predicate ?object} ", URI);
        boolean result = SPARQL.askQuery(URI, query);
        return result;
    }


    public static boolean hasMachineReadableLicense(String URI){
        String[] licensePredicates = {
                "<http://purl.org/dc/terms/license>",
                "<http://purl.org/dc/terms/rights>",
                "<http://purl.org/dc/elements/1.1/rights>",
                "<http://www.w3.org/1999/xhtml#license>",
                "<http://creativecommons.org/ns#license>",
                "<http://purl.org/dc/elements/1.1/licence>",
                "<http://usefulinc.com/ns/doap#license>",
                "<http://schema.org/license>",

                };
        String joinedPredicates = String.join("|", licensePredicates);
        String query = String.format("ASK {?s %s ?o}", joinedPredicates);
        boolean result = SPARQL.askQuery(URI, query);
        return result;

    }

    public static boolean hasBasicProvenance(String URI){
        try{
            if (!DereferenceURI.accessRDF(URI)){
                return true;
            }
            String query = "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                    "PREFIX dcterms: <http://purl.org/dc/terms/>" +
                    "ASK { ?s dc:publisher|dc:creator|foaf:maker|dcterms:creator ?o}";
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        }
        catch(Exception e){
            System.out.println(e);
            return true;
        }
    }

    public static boolean hasHumanReadableLicense(String URI){
        try{
            if (!DereferenceURI.accessRDF(URI)){
                return true;
            }
            String[] licensePredicates = {
                    "<http://purl.org/dc/terms/description>",
                    "<http://www.w3.org/2000/01/rdf-schema#comment>",
                    "<http://www.w3.org/2000/01/rdf-schema#label>",
                    "<http://schema.org/description>"
            };
            String regularExpression = "(licensed|rights|copyrighted|license)+";
            String joinedPredicates = String.join("|", licensePredicates);
            String query = String.format("ASK {  \n" +
                    "  ?subject %s ?object. \n" +
                    "  FILTER regex(?object, \"(licensed|rights|copyrighted|license)+\"). \n" +
                    "} ", joinedPredicates );
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        }
        catch(Exception e){
            System.out.println(e);
            return true;
            }
        }

        public static boolean hasHumamReadableLabelling(String URI){
            try{
                if (!DereferenceURI.accessRDF(URI)){
                    return true;
                }
                String[] labellingPredicates = new String[]{"rdfs:label", "dcterms:title", "dcterms:description",
                        "dcterms:alternative", "skos:altLabel", "skos:prefLabel", "powder-s:text",
                        "skosxl:altLabel", "skosxl:hiddenLabel", "skosxl:prefLabel", "skosxl:literalForm",
                        "schema:description", "schema:description", "foaf:name" };
                String joinedPredicates = String.join("|", labellingPredicates);
                String query = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "PREFIX dcterms: <http://purl.org/dc/terms/> \n" +
                        "PREFIX ct: <http://data.linkedct.org/resource/linkedct/>\n" +
                        "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                        "PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>\n" +
                        "PREFIX schema: <http://schema.org/>\n" +
                        "PREFIX powder-s: <http://www.w3.org/2007/05/powder-s#> \n" +
                        "\n" +
                        "ASK {<%s> %s ?object} ", URI, joinedPredicates);
                boolean result = SPARQL.askQuery(URI, query);
                return result;
            }
            catch(Exception e){
                System.out.println(e);
                return true;
            }
        }

    public static boolean getRDF(String string_URL) {
        try {
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            connection.setRequestProperty("Accept", "application/rdf+xml");
            int code = connection.getResponseCode();
            System.out.println(code);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static boolean test(String URI){
        Model model = ModelFactory.createDefaultModel().read(URI);
        String queryString = "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                "PREFIX dcterms: <http://purl.org/dc/terms/>" +
                "SELECT ?s { ?s dc:creator ?o}";
        Query query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(queryString, model)) {
            ResultSet results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
               // RDFNode s = soln.get("?s") ;       // Get a result variable by name.
                RDFNode o = soln.get("?o") ;       // Get a result variable by name.
                RDFNode p = soln.get("?s") ;       // Get a result variable by name.
                System.out.println(String.format("  %s %s", o.toString(), p.toString()));
            }
        }
        return true;
    }

}

