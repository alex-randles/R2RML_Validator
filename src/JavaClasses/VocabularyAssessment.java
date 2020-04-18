package JavaClasses;

import java.net.HttpURLConnection;
import java.net.URL;

public class VocabularyAssessment {

    public static boolean isAccessible(String URI){
        boolean result =  DereferenceURI.accessRDF(URI);
        return result;
    }

    public static boolean validateUndefined(String URI){
        try{
            String query = String.format("ASK {<%s> ?predicate ?object} ", URI);
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        }
        catch(Exception e){
            return false;
        }
    }

    public static boolean hasMachineReadableLicense(String URI){
        try{
            String query = "PREFIX schema: <http://schema.org/>\n" +
                    "PREFIX doap: <http://usefulinc.com/ns/doap#>\n" +
                    "PREFIX cc: <http://creativecommons.org/ns#>\n" +
                    "PREFIX xhtml: <http://www.w3.org/1999/xhtml#>\n" +
                    "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
                    "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                    "ASK {?s dct:license|dct:rights|dc:rights|xhtml:license|cc:license|dc:licence|doap:license|schema:license ?o }";
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        }
        catch(Exception e){
            return true;
        }
    }

    public static boolean hasBasicProvenance(String URI){
        try{
            String query = "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                    "PREFIX dcterms: <http://purl.org/dc/terms/>" +
                    "ASK { ?s dc:publisher|dc:creator|foaf:maker|dcterms:creator ?o}";
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        }
        catch(Exception e){
            return true;
        }
    }

    public static boolean hasHumanReadableLicense(String URI) {
        try {
            String[] licensePredicates = {
                    " dc:description",
                    "rdfs:comment",
                    "rdfs:label",
                    "schema:description"
            };
            String joinedPredicates = String.join("|", licensePredicates);
            String query = String.format("PREFIX schema: <http://schema.org/>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                    "ASK {  \n" +
                    "  ?subject %s ?object. \n" +
                    "   FILTER regex(STR(?object), \".*(licensed?|copyrighte?d?).*\" ). \n \n" +
                    "} ", joinedPredicates);
            boolean result = SPARQL.askQuery(URI, query);
            return result;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean hasDomainRangeDefinition(String URI){
        try{
            String query = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "ASK { <%s> rdfs:domain ?domain;" +
                    "           rdfs:range ?range }", URI);
            return SPARQL.askQuery(URI, query);
        }
        catch(Exception e){
            return true;
        }
    }

    public static boolean hasHumamReadableLabelling(String URI){
            try{
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
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

}

