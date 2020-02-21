package JavaClasses;


import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;
import org.apache.jena.vocabulary.RDFS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public class OntologyQualityAssessment {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";

    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        runTest(FOAF_NS);

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

    public static boolean providesHTML(String URL){
            return true;
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

    public static boolean isAccessible(String URI){
        boolean s =  DereferenceURI.accessRDF(URI);
        System.out.println(s);
        return s;
    }

    public static void runTest(String URI) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Populate commands map
        if(!isAccessible(URI)){
            writeData("Mapping definition not accessible");
            return;
        }
        int numSubjects = subjectCount(URI);
//        Map<String, Runnable> commands = new HashMap<>();
//
////        commands.put("Triples with human labelling ", () -> numHumanLabelling(URI));
////        commands.put("Triples with Domain definition ", () -> numDomainDefinition(URI));
////        commands.put("Triples with range definition ", () -> numRangeDefinition(URI));
////        commands.put("Triples with defined by ", () -> numDefinedBy(URI));
//        // commands.get("Accessibility").run();   // Prints "Teleport"
//        Iterator it = commands.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            int testResult = commands.get((String) pair.getKey()).run(URI);
//            System.out.println((String) pair.getKey() +  "3636");
//            it.remove(); // avoids a ConcurrentModificationException
//        }

//        System.out.println(numRangeDefinition(URI));
//        System.out.println(((float)numRangeDefinition(URI)/numSubjects)*100);
       // System.out.println(String.format("Triples with range definition %.0f%%", ((float)numRangeDefinition(URI)/numSubjects)*100));
        writeData(String.format("Ontology %s  - Triples with range definition %.0f%%\n", URI, ((float)numRangeDefinition(URI)/numSubjects)*100));
        writeData(String.format("Ontology %s  - Triples with domain definition %.0f%%\n", URI, ((float)numDomainDefinition(URI)/numSubjects)*100));
        writeData(String.format("Ontology %s  - Triples with human labelling %.0f%%\n", URI, ((float)numHumanLabelling(URI)/numSubjects)*100));
        writeData(String.format("Ontology %s  - Triples with is defined by definition %.0f%%\n", URI, ((float)numDefinedBy(URI)/numSubjects)*100));

    }

    public static void writeData(String content) throws IOException {
        File file = new File("OntologyAssessmentResults.txt");
        FileWriter fr = new FileWriter(file, true);
        fr.write(content);
        fr.close();
    }

    public static int subjectCount(String URI) {
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



}

