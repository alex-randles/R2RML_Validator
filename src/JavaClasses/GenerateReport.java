package JavaClasses;

import org.apache.jena.query.*;

import java.util.ArrayList;

public class GenerateReport {


    public static String R2RML_NS = "http://www.w3.org/ns/r2rml#";
    public static String outputFileName = "./resources/output.ttl";
    public static String mappingFileName = "./resources/sample_map.ttl";
    public static String domainShape = "http://www.w3.org/ns/r2rml#DomainShape";


    public static void main(String[] args) {

        querySHACLOutput("./resources/output.ttl");
//        selectSourceShape(outputFileName);
//        getNumberOccurrences(mappingFileName, R2RML_NS+"class");

    }

    public static void querySHACLOutput(String fileName) {
        if (!shapeConforms(fileName)) {
            ArrayList<String> shapes = selectSourceShape(outputFileName);
            for(String s: shapes){
                System.out.println(isDomainShape(s));
            }
        }
    }

    public static boolean isDomainShape(String shape){
        if(shape.equals(domainShape)){
            return true;
        }
        return false;
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

    public static int getNumberOccurrences(String fileName, String predicate){
        String query = String.format("SELECT ?predicate WHERE { ?s <%s> ?predicate}", predicate);
        ResultSet results = SPARQL.selectQuery(fileName, query);
        int count = 0;
        for(;results.hasNext();){
            QuerySolution soln = results.nextSolution();
            count++;
        }
        System.out.println(count);
        return count;
    }



}