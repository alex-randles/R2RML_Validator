package JavaClasses;


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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OntologyQualityAssessment {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";

    public static void main(String[] args) throws IOException {
        numDomainDefinition(FOAF_NS);
        numRangeDefinition(RDFS_NS);

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

    public static boolean isAccessible(String URI){
        boolean s =  DereferenceURI.accessRDF(URI);
        System.out.println(s);
        return s;
    }

    public static void runTest(String URI) throws IOException {
        // Populate commands map
        if(!isAccessible(URI)){
            writeData("Mapping definition not accessible");
            return;
        }
        Map<String, Runnable> commands = new HashMap<>();
        int distintSubjectCount = subjectCount(URI);

        commands.put("Triples with human labelling %s", () -> hasHumanLabelling(URI));
        commands.put("Triples with Domain definition %s", () -> hasDomain(URI));
        commands.put("Triples with range definition %s", () -> hasRange(URI));

        // commands.get("Accessibility").run();   // Prints "Teleport"
        Iterator it = commands.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String currentTest = String.format((String) pair.getKey(), 5);
            System.out.println(currentTest);
            it.remove(); // avoids a ConcurrentModificationException
        }
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
                System.out.println(String.format("URI %s as %s triples", URI, numSubjects));

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
                int numSubjects = Integer.parseInt(count.split("\\^")[0]);
                System.out.println(String.format("URI %s has range %s triples", URI, numSubjects));

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
}

