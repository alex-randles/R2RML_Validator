package JavaClasses;

import org.apache.jena.query.ResultSet;

public class DataType {


    public static String getCorrectDatatype(String URI){
        String selectQuery = String.format("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?datatype \n" +
                "WHERE { <%s> rdfs:range ?datatype} ", URI);
        ResultSet selectResult = SPARQL.selectQuery(URI, selectQuery);
        return SPARQL.getStringVariable(selectResult, "?datatype");
    }

    public static boolean validateDatatype(String predicate, String datatype) {
        try {
            String range = getCorrectDatatype(predicate);
            if (!range.split("#")[0].equals(NS.XSD_NS) || range.isEmpty()) {
                return true;
            }
            else if (datatype.equals("undefined")){
                Refinement.addDataTypeTriple(range, predicate, FileNames.originalMappingFile);
                return true;
            }
            else if (!range.equals(datatype)){
                Refinement.changeDataTypeTriple(range, datatype, predicate, FileNames.originalMappingFile);
                return false;
            }
            return true;
        }
        catch (Exception e){
            return true;
        }
    }

}
