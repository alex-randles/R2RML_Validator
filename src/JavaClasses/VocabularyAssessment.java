package JavaClasses;

import org.apache.jena.query.*;

import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL;
// import  org.apache.commons.httpclient.*;


public class VocabularyAssessment {


    public static void main(String[] args) {
        // calculate number of unique subjects with human labellings
        //System.out.println(hasHumamReadableLabelling("http://dbpedia.org/ontology/Person")) ;
        // System.out.println(vocabularyCompleteness("http://dbpedia.org/ontology/djdjdjd"));
        System.out.println(getRDF(NS.FOAF_NS+"age"));

    }

    public static boolean isAccessible(String URI){
        // check if resource is accessible
        boolean result =  DereferenceURI.accessRDF(URI);
        return result;
    }


    public static int subjectCount(String URI) {
        // calculate total number of unique subjects
        String query = "SELECT (COUNT(DISTINCT ?s) AS ?triples) WHERE { ?s ?p ?o }";
        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String count = soln.getLiteral("?triples").toString();   // Get a result variable - must be a literal
            if (!count.isEmpty()) {
                int numSubjects = Integer.parseInt(count.split("\\^")[0]);
                System.out.println(String.format("URI %s has %s unique subjects", URI, numSubjects));

                return numSubjects;
            }

        }
        return 0;
    }

    public static int numRangeDefinition(String URI) {
        // calculate number of unique subjects with range definitions
        String query = "SELECT (COUNT(DISTINCT ?s) AS ?triples) WHERE { ?s <http://www.w3.org/2000/01/rdf-schema#range> ?o }";
        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String count = soln.getLiteral("?triples").toString();   // Get a result variable - must be a literal
            if (!count.isEmpty()) {
                int numRange = Integer.parseInt(count.split("\\^")[0]);
                System.out.println(String.format("URI %s has range %s triples", URI, numRange));
                return numRange;
            }

        }
        return 0;
    }

    public static int numDefinedBy(String URI) {
        // calculate number of unique subjects with defined by
        String query = "SELECT (COUNT(DISTINCT ?s) AS ?triples) WHERE { ?s <http://www.w3.org/2000/01/rdf-schema#isDefinedBy> ?o }";
        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String count = soln.getLiteral("?triples").toString();   // Get a result variable - must be a literal
            if (!count.isEmpty()) {
                int numSubjects = Integer.parseInt(count.split("\\^")[0]);
                System.out.println(String.format("URI %s has defined by %s triples", URI, numSubjects));

                return numSubjects;
            }

        }
        return 0;
    }

    public static int numDomainDefinition(String URI){
            // calculate number of unique subjects with domain definitions
            String query = "SELECT (COUNT(DISTINCT ?s) AS ?triples) WHERE { ?s <http://www.w3.org/2000/01/rdf-schema#domain> ?o }";
            ResultSet results = SPARQL.selectQuery(URI, query);
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                String count = soln.getLiteral("?triples").toString();   // Get a result variable - must be a literal
                if (!count.isEmpty()) {
                    int numSubjects = Integer.parseInt(count.split("\\^")[0]);
                    System.out.println(String.format("URI %s has domain %s triples", URI, numSubjects));

                    return numSubjects;
                }

            }
            return 0;
    }

    public static int numHumanLabelling(String URI){
        // calculate number of unique subjects with human labellings
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX dcterms: <http://purl.org/dc/terms/> \n" +
                "PREFIX ct: <http://data.linkedct.org/resource/linkedct/>\n" +
                "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>\n" +
                "PREFIX schema: <http://schema.org/>\n" +
                "PREFIX powder-s: <http://www.w3.org/2007/05/powder-s#> \n" +
                "\n" +
                "SELECT (COUNT( DISTINCT ?subject) as ?count) \n" +
                "WHERE {\n" +
                "   OPTIONAL { ?subject rdfs:label ?label }.\n" +
                "   OPTIONAL { ?subject dcterms:title ?label }.\n" +
                "   OPTIONAL { ?subject dcterms:description ?label }.\n" +
                "   OPTIONAL { ?subject dcterms:alternative ?label }.\n" +
                "   OPTIONAL { ?subject skos:altLabel ?label }.\n" +
                "   OPTIONAL { ?subject skos:prefLabel ?label }.\n" +
                "   OPTIONAL { ?subject powder-s:text ?label }.\n" +
                "   OPTIONAL { ?subject skosxl:altLabel?label }.\n" +
                "   OPTIONAL { ?subject skosxl:hiddenLabel ?label }.\n" +
                "   OPTIONAL { ?subject skosxl:prefLabel ?label }.\n" +
                "   OPTIONAL { ?subject skosxl:literalForm ?label }.\n" +
                "    OPTIONAL { ?subject schema:description ?label }.\n" +
                "    OPTIONAL { ?subject schema:alternateName ?label }.\n" +
                "    OPTIONAL { ?subject foaf:name ?label }.\n" +
                "\n" +
                "}\n";

        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String count = soln.getLiteral("?count").toString();   // Get a result variable - must be a literal
            if (!count.isEmpty()) {
                int numLabels = Integer.parseInt(count.split("\\^")[0]);
                System.out.println(String.format("URI %s has labelling %s triples", URI, numLabels));
                return numLabels;
            }

        }
        return 0;
    }


    public static boolean lowLatency(String URI){
        // if vocabulary not available, return true
        if(!DereferenceURI.getResponseCode(URI)){
            return true;
        }
        // ideal time is 1 seconds
        // return true if <= else false
        double idealTime = 1.0;
        long startTime = System.nanoTime();
        boolean response  = DereferenceURI.getResponseCode(URI);
        long elapsedTime = System.nanoTime() - startTime;
        double elapsedSeconds = (double)elapsedTime / 1_000_000_000.0;
        if (response && elapsedSeconds < idealTime){
            return true;
        }
        return false;
    }


    public static boolean vocabularyCompleteness(String URI){
        // returns true if vocabulary contains definition relating to URI
        // if can not retrieve vocabulary, return true
        if (!DereferenceURI.accessRDF(URI)){
            return false;
        }
        String query = String.format("ASK {<%s> ?predicate ?object} ", URI);
        System.out.println(query);
        boolean result = SPARQL.askQuery(URI, query);
        return result;


    }
    public static boolean hasMachineReadableLicense(String URI){
        // returns true if machine readable license present
        // http://purl.org/NET/rdflicense/.* as predicate and object is a valid license
        String[] licensePredicates = {
                "<http://purl.org/dc/terms/license>",
                "<http://purl.org/dc/terms/rights>",
                "<http://purl.org/dc/elements/1.1/rights>",
                "<http://www.w3.org/1999/xhtml#license>",
                "<http://creativecommons.org/ns#license>",
                "<http://purl.org/dc/elements/1.1/licence>",
                "<http://usefulinc.com/ns/doap#license>",
                "<http://schema.org/license>"
                };
        String joinedPredicates = String.join("|", licensePredicates);
        String query = String.format("ASK {?s %s ?o}", joinedPredicates);
        boolean result = SPARQL.askQuery(URI, query);
        System.out.println(result);
        return true;

    }

    public static boolean hasHumanReadableLicense(String URI){
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

        public static boolean highThroughput(String URI){
//            MultiThreadedHttpConnectionManager connectionManager =
//                    new MultiThreadedHttpConnectionManager();
//            HttpClient client = new HttpClient(connectionManager);
//            // and then from inside some thread executing a method
            return true;



        }

        public static boolean hasHumamReadableLabelling(String URI){
            // if can not retrieve vocabulary, return true
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

}

