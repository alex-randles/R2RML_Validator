package JavaClasses;

import org.apache.jena.query.ResultSet;

public class DataType {

    public static boolean validateDatatype(String predicateURI) {
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
            if (!range.split("#")[0].equals("http://www.w3.org/2001/XMLSchema") || range.isEmpty()) {
                return true;
            }
            if(!hasDatatype){
                Refinement.addDataTypeTriple(range, predicateURI, FileNames.refinedMappingFile);
                return true;
            }
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
