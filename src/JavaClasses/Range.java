package JavaClasses;

import org.apache.jena.query.ResultSet;

public class Range {

    public static boolean validateRange(String predicateURI, String termType){
        try{
            String range = getRange(predicateURI);
            if(range.isEmpty()){
                return true;
            }
            boolean isLiteral = isDatatype(range);
            boolean correctRange;
            if(isLiteral){
                correctRange = termType.equals(NS.R2RML_NS + "Literal");
                if(!correctRange){
                    Refinement.changeTermType(predicateURI, NS.R2RML_NS + "Literal");
                }
            }
            else {
               correctRange = termType.equals(NS.R2RML_NS + "IRI");
                if(!correctRange){
                    Refinement.changeTermType(predicateURI, NS.R2RML_NS + "IRI");
                }
            }
            return correctRange;
        }
        catch(Exception e){
            return true;
        }
    }

    public static boolean isDatatype(String range){
        return range.split("#")[0].equals(NS.XSD_NS);
    }

    public static String getRange(String predicateURI){
        String query = String.format("" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "SELECT ?range WHERE {<%s> rdfs:range ?range} ", predicateURI);
        ResultSet results = SPARQL.selectQuery(predicateURI, query);
        String range = SPARQL.getStringVariable(results, "?range");
        return range;
    }

//    public static String getTermType(String predicateURI){
//        String selectQuery = String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
//                "SELECT ?termType \n" +
//                "WHERE {\n" +
//                "  ?subject rr:predicateObjectMap ?predicateObjectMap. \n" +
//                "  ?predicateObjectMap rr:predicate <%s>.\n" +
//                "  ?predicateObjectMap rr:objectMap ?objectMap. \n" +
//                "  ?objectMap rr:termType ?termType. \n" +
//                "} ", predicateURI);
//        ResultSet results = SPARQL.selectQuery(FileNames.originalMappingFile, selectQuery);
//        String termType = SPARQL.getStringVariable(results, "?termType");
//        return termType;
//    }
}
