package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.io.File;

public class Range {

    public static boolean validateRange(String predicateURI){
        try{
            String range = getRange(predicateURI);
            String termType = getTermType(predicateURI);
            if(range.isEmpty() || termType.isEmpty() || termType.equals("http://www.w3.org/ns/r2rml#BlankNode")){
                return true;
            }
            boolean isLiteral = isDatatype(range);
            boolean correctRange;
            if(isLiteral){
                correctRange = termType.equals("http://www.w3.org/ns/r2rml#Literal");
                if(!correctRange){
                    Refinement.changeTermType(predicateURI, "http://www.w3.org/ns/r2rml#Literal");
                }
            }
            else {
               correctRange = termType.equals("http://www.w3.org/ns/r2rml#IRI");
                if(!correctRange){
                    Refinement.changeTermType(predicateURI, "http://www.w3.org/ns/r2rml#IRI");
                }
            }
            return correctRange;
        }
        catch(Exception e){
            return true;
        }
    }

    public static boolean isDatatype(String range){
        return range.split("#")[0].equals("http://www.w3.org/2001/XMLSchema");
    }

    public static String getRange(String predicateURI){
        String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range} ", predicateURI);
        ResultSet results = SPARQL.selectQuery(predicateURI, query);
        String range = SPARQL.getStringVariable(results, "?range");
        return range;
    }

    public static String getTermType(String predicateURI){
        String selectQuery = String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                "SELECT ?termType \n" +
                "WHERE {\n" +
                "  ?subject rr:predicateObjectMap ?predicateObjectMap. \n" +
                "  ?predicateObjectMap rr:predicate <%s>.\n" +
                "  ?predicateObjectMap rr:objectMap ?objectMap. \n" +
                "  ?objectMap rr:termType ?termType. \n" +
                "} ", predicateURI);
        ResultSet results = SPARQL.selectQuery(FileNames.originalMappingFile, selectQuery);
        String termType = SPARQL.getStringVariable(results, "?termType");
        return termType;
    }
}
