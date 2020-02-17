package JavaClasses;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

import java.io.File;
import java.io.FileOutputStream;

public class OntologyQualityAssessment {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";

    public static void main(String[] args) {
        System.out.println(subjectCount(FOAF_NS));
    }

    public static boolean hasRange(String URI) {
        String query = String.format("ASK {<%s> <%s> ?domain }", URI, RDFS_NS + "range");
        boolean result = SPARQL.askQuery(URI, query);
        return result;
    }

    public static boolean hasDomain(String URI) {
        String query = String.format("ASK {<%s> <%s> ?domain }", URI, RDFS_NS + "domain");
        boolean result = SPARQL.askQuery(URI, query);
        return result;

    }

    public static boolean hasHumanLabelling(String URI) {
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
        for (String current_label : humanLabels) {
            String query = String.format("ASK {<%s> <%s> ?label }", URI, current_label);
            boolean result = SPARQL.askQuery(URI, query);
            if (result) {
                return result;
            }
        }
        return false;

    }

    public static int subjectCount(String URI) {
        String query = "SELECT (COUNT(DISTINCT ?s) AS ?triples) WHERE { ?s ?p ?o }";
        ResultSet results = SPARQL.selectQuery(URI, query);
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            String count = soln.getLiteral("?triples").toString();   // Get a result variable - must be a literal
            if (!count.isEmpty()) {
                System.out.println(String.format("URI %s as %s triples", URI, count));
                int numSubjects = Integer.parseInt(count);
                return numSubjects;
            }

        }
        return 0;
    }

}
