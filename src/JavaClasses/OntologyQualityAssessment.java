package JavaClasses;


public class OntologyQualityAssessment {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#" ;
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    public static void main(String[] args){
        System.out.println(hasHumanLabelling(FOAF_NS+"firstName"));
    }

    public static boolean hasRange(String URI){
        String query = String.format("ASK {<%s> <%s> ?domain }", URI, RDFS_NS+"range");
        boolean result = SPARQL.askQuery(URI,query);
        return result;
    }

    public static boolean hasDomain(String URI){
        String query = String.format("ASK {<%s> <%s> ?domain }", URI, RDFS_NS+"domain");
        boolean result = SPARQL.askQuery(URI,query);
        return result;

    }

    public static boolean hasHumanLabelling(String URI){
        String[] humanLabels = new String[]{"rdfs:label",
                "rdfs:comment",
                "dcterms:title",
                "dcterms:description",
                "dcterms:alternative",
                "skos:altLabel",
                "skos:prefLabel",
                "skos:note",
                "powder-s:text",
                "skosxl:altLabel",
                "skosxl:hiddenLabel",
                "skosxl:prefLabel",
                "skosxl:literalForm",
                "schema:name",
                "schema:description",
                "schema:alternateName",
                "foaf:name"};
        for (String current_label :humanLabels){
            String query = String.format("ASK {<%s> <%s> ?label }", URI, current_label);
            boolean result = SPARQL.askQuery(URI,query);
            if(result){
                return result;
            }
        }
        return false;

    }
}
