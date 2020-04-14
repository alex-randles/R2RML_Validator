package JavaClasses;

import org.apache.jena.query.ResultSet;

public class DuplicateTriples {

    public static boolean detectDuplicateTriples(){
        boolean result1  = duplicateTriples1(FileNames.originalMappingFile);
        boolean result2 = duplicateTriplesCase2(FileNames.originalMappingFile);
        boolean result3 = duplicateTriplesCase3(FileNames.originalMappingFile);
        if(result1 || result2 || result3){
            return false;
        }
        return true;
    }

    public static boolean duplicateTriples1(String URI){
        String query = "\n" +
                "\n" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#> " +
                "SELECT (COUNT(?predicateObjectMap) AS ?count)  ?subject ?predicate ?column ?template ?datatype ?termType ?language\n" +
                "WHERE {\n" +
                "  ?subject \t rr:predicateObjectMap ?predicateObjectMap.\n" +
                "  ?predicateObjectMap rr:predicate ?predicate.\n" +
                "  ?predicateObjectMap rr:objectMap ?objectMap. \n" +
                "  OPTIONAL {?objectMap     rr:column ?column. }\n" +
                "  OPTIONAL {?objectMap    rr:template  ?template. }\n" +
                "  OPTIONAL {?objectMap    rr:datatype ?datatype. }\n" +
                "  OPTIONAL {?objectMap     rr:termType ?termType. }\n" +
                "  OPTIONAL {?objectMap     rr:language ?language. }\n" +
                "  OPTIONAL {?objectMap     rr:parentTriplesMap ?parentTriplesMap. }\n" +
                "  OPTIONAL {?objectMap     rr:joinCondition ?joinCondition. }\n" +
                "  OPTIONAL {?joinCondition     rr:child ?child. }" +
                "  OPTIONAL {?joinCondition     rr:parent ?parent. }" +
                "}\n" +
                "GROUP BY ?subject ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap  ?child ?parent \n" +
                "HAVING (COUNT(?predicateObjectMap)  > 1)\n" +
                "\n" +
                "\n";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");
        return !count.isEmpty();
    }

    public static boolean duplicateTriplesCase2(String URI){
        String query = "" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
                "SELECT (COUNT(?predicateObjectMap) as ?count) ?predicateMapConstant ?objectMapConstant\n" +
                "WHERE{\n" +
                "  ?subject rr:predicateObjectMap ?predicateObjectMap.\n" +
                "  ?predicateObjectMap rr:predicateMap ?predicateMap.\n" +
                "  ?predicateObjectMap rr:objectMap ?objectMap. \n" +
                "  ?predicateMap rr:constant ?predicateMapConstant. \n" +
                "  ?objectMap rr:constant ?objectMapConstant\n" +
                "}\n" +
                "GROUP BY ?subject ?predicateMapConstant ?objectMapConstant\n" +
                "HAVING(COUNT(?predicateObjectMap) > 1) ";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");
        return !count.isEmpty();
    }

    public static boolean duplicateTriplesCase3(String URI){
        String query = "" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#> \n" +
                "SELECT  (COUNT(?predicateObjectMap) AS ?count)\n" +
                "WHERE {\n" +
                "  ?subject       rr:predicateObjectMap ?predicateObjectMap.\n" +
                "  ?predicateObjectMap rr:predicate ?predicate.\n" +
                "  ?predicateObjectMap rr:object ?object . \n" +
                "}\n" +
                "GROUP BY ?subject ?predicate ?object\n" +
                "HAVING (COUNT(?predicateObjectMap)  > 1)\n";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");
        return !count.isEmpty();
    }

}
