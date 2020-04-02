package JavaClasses;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class Range {

//    public static boolean validateRange(String predicateURI, String dataTypeURI){
//        try{
//            String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range} ", predicateURI);
//            ResultSet results = SPARQL.selectQuery(predicateURI, query);
//            for (; results.hasNext(); ) {
//                QuerySolution soln = results.nextSolution();
//                String range = soln.get("?range").toString();   // Get a result variable - must be a literal
//                if (!range.isEmpty()) {
//                    String[] parts = range.split("#");
//                    if (parts[0].equals("http://www.w3.org/2001/XMLSchema")) {
//                        if(!range.equals(dataTypeURI)){
//                            return false;
//                        }
//
//                    }
//                }
//            }
//            return true;
//        }
//        catch(Exception e){
//            System.out.println(e + "validating range");
//        }
//        return true;
//    }

    public static String mappingFile = "./resources/sample_map.ttl";

    public static void main(String[] args){
        boolean test = validateRange("http://dbpedia.org/ontology/club");
        System.out.println(test);
    }

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
            System.out.println(e + "validating range");
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
        ResultSet results = SPARQL.selectQuery(mappingFile, selectQuery);
        String termType = SPARQL.getStringVariable(results, "?termType");
        return termType;
    }
}
