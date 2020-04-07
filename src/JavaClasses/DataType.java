package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.io.File;

public class DataType {

    public static void main(String[] args){
        String selectQuery = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?datatype \n" +
                "WHERE { <%s> rdfs:range ?datatype} ", "http://www.txample.com/people/voc/department");
        ResultSet selectResult = SPARQL.selectQuery("http://www.txample.com/people/voc/department", selectQuery);
        String range = SPARQL.getStringVariable(selectResult, "?datatype");
        System.out.println("dhdh");
    }
    public static boolean validateDatatype(String predicateURI) {
        // if can not retrieve vocabulary, return true
        try {
            String hasDataTypeQuery = String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#> ASK {  ?subject  rr:predicateObjectMap ?predicateObjectMap. " +
                    "?predicateObjectMap rr:predicate <%s>. " +
                    " ?predicateObjectMap rr:objectMap ?objectMap. ?objectMap rr:datatype ?datatype }\n", predicateURI);
            boolean hasDatatype = SPARQL.askQuery(FileNames.originalMappingFile, hasDataTypeQuery);
            String selectQuery = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?datatype \n" +
                    "WHERE { <%s> rdfs:range ?datatype} ", predicateURI);
            ResultSet selectResult = SPARQL.selectQuery(predicateURI, selectQuery);
            String range = SPARQL.getStringVariable(selectResult, "?datatype");
            // if range is not a datatype or not defined, return true
            System.out.println(predicateURI);
            if (!range.split("#")[0].equals("http://www.w3.org/2001/XMLSchema") || range.isEmpty()) {
                System.out.println("is not a datatype");
                return true;
            }
            if(!hasDatatype){
                System.out.println("adding missing datatype ");
                Refinement.addDataTypeTriple(range, predicateURI, FileNames.refinedMappingFile);
                return true;
            }
            System.out.println("datatype for " + predicateURI + " is " + range);
            String askQuery = String.format("" +
                    "PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "ASK\n" +
                    "{\n" +
                    "  ?subject  rr:predicateObjectMap ?predicateObjectMap.\n" +
                    "  ?predicateObjectMap rr:predicate <%s>.\n" +
                    "  ?predicateObjectMap rr:objectMap ?objectMap. \n" +
                    "  ?objectMap rr:datatype <%s>. \n" +
                    "}\n", predicateURI, range);
            boolean correctDatatype = SPARQL.askQuery(FileNames.originalMappingFile, askQuery);
            if (!correctDatatype) {
                Refinement.changeDataTypeTriple(range, predicateURI, FileNames.refinedMappingFile);
            }
            return correctDatatype;
        }
        catch (Exception e){
            return true;
        }
    }

}
