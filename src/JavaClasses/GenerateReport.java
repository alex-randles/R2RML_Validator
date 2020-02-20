package JavaClasses;

import org.apache.jena.query.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateReport {


    public static String R2RML_NS = "http://www.w3.org/ns/r2rml#";
    public static String outputFileName = "./resources/output.ttl";
    public static String mappingFileName = "./resources/sample_map.ttl";
    public static String domainShape = R2RML_NS + "DomainShape";
    public static String disjointShape = R2RML_NS + "DisjointShape";
    public static String resourceDereferencableShape = R2RML_NS + "ResourceDereferencableShape";
    public static String blankNodeShape = R2RML_NS + "BlankNodeShape";
    public static String termTypeShape = R2RML_NS + "TermTypeShape";
    public static String[] reportShapes =  {domainShape, disjointShape, resourceDereferencableShape};
    public static Map<String, String> headings = new HashMap<String, String>();
    public static String SHACL_NS = "http://www.w3.org/ns/shacl#";
    public static String[] columnNames = {"number of classes", "number of predicates", };
    public static String reportFile = "./resources/report.csv";

    public static void main(String[] args) throws IOException {
        generateReport(outputFileName, mappingFileName);
    }

    public static void generateReport(String shaclOutputFile, String mappingFile) throws IOException {
        writeFile(reportFile, String.join(",", columnNames));
        headings.put(blankNodeShape, "number of blank nodes ");
        headings.put(disjointShape, "number of disjoint classes ");
        headings.put(domainShape, "number of domain violations ");
        for (Map.Entry mapElement : headings.entrySet()) {
            String value = (String)mapElement.getValue();
            writeValue(reportFile, value+",");
        }
        writeFile(reportFile, "\n");
        writeValue(reportFile, String.valueOf(getNumberOccurrences(mappingFile, R2RML_NS+"predicates")));
        writeValue(reportFile, String.valueOf(getNumberOccurrences(mappingFile, R2RML_NS+ "class")));
        // System.out.println(getNumberShapes(shaclOutputFile, R2RML_NS + "DomainShape"));
        for (Map.Entry mapElement : headings.entrySet()) {
            String key = (String)mapElement.getKey();
            String s = String.valueOf(getNumberShapes(shaclOutputFile, key));
            System.out.println(key + s);
            // writeValue(reportFile, String.valueOf(getNumberShapes(shaclOutputFile, R2RML_NS + "DomainShape")));
        }
    }


    public static void writeValue(String outputFile, String content) throws IOException {
        File file = new File(outputFile);
        FileWriter fr = new FileWriter(file, true);
        fr.write(content + ",");
        fr.close();
    }

    public static void writeFile(String outputFile, String content) throws IOException {
        File file = new File(outputFile);
        FileWriter fr = new FileWriter(file, true);
        fr.write(content + ",");
        fr.close();
    }

    public static void querySHACLOutput(String fileName) {
        if (!shapeConforms(fileName)) {
            ArrayList<String> shapes = selectSourceShape(outputFileName);
            Map<String, Integer> counts = new HashMap<String, Integer>();
            for(String shape : shapes){
                if(counts.get(shape)==null){
                    counts.put(shape, 1);
                }
                else{
                    int currentValue = (int) counts.get(shape) ;
                    int newValue = currentValue + 1;
                    counts.put(shape, newValue);
                }
            }
            for(Map.Entry<String, Integer> entry : counts.entrySet() ){

            }

        }
    }

    public static void incrementEntry(Map map, String key){
        if(map.get(key)==null){
            map.put(key, 1);
        }
        else{
            int currentValue = (int) map.get(key);
            map.put(key, currentValue++);
        }
    }

    public static boolean isDomainShape(String shape){
        return shape.equals(domainShape);

    }

    public static boolean isDisjointShape(String shape){
        return shape.equals(disjointShape);
    }

    public static boolean isResourceDereferencableShape(String shape){
        return shape.equals(resourceDereferencableShape);
    }

    public static boolean shapeConforms(String fileName) {
        String query = "SELECT (str(?o) AS ?conforms) WHERE {?s <http://www.w3.org/ns/shacl#conforms> ?o}";
        ResultSet results = SPARQL.selectQuery(fileName, query);
        String conforms = SPARQL.getStringVariable(results, "?conforms");
        System.out.println("This shapes conformity "  + conforms);
        return Boolean. parseBoolean(conforms);
    }


    public static ArrayList<String> selectSourceShape(String fileName) {
        String query = "SELECT ?shape WHERE { ?s <http://www.w3.org/ns/shacl#sourceShape> ?shape}";
        ResultSet results = SPARQL.selectQuery(fileName, query);
        ArrayList<String> shapesList = new ArrayList<String>();
        for(;results.hasNext();){
            QuerySolution soln = results.nextSolution();
            String s = soln.get("?shape").toString();
            shapesList.add(s);
        }
        return shapesList;
    }


    public static int getNumberShapes(String fileName, String shape){
        String query = String.format("SELECT ?predicate WHERE { ?s ?p <%s>}", shape);
        ResultSet results = SPARQL.selectQuery(fileName, query);
        int count = 0;
        for(;results.hasNext();){
            QuerySolution soln = results.nextSolution();
            count++;
        }
        return count;
    }
    public static int getNumberOccurrences(String fileName, String predicate){
        String query = String.format("SELECT ?predicate WHERE { ?s <%s> ?predicate}", predicate);
        ResultSet results = SPARQL.selectQuery(fileName, query);
        int count = 0;
        for(;results.hasNext();){
            QuerySolution soln = results.nextSolution();
            count++;
        }
        return count;
    }



}