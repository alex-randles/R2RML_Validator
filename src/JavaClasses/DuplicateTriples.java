package JavaClasses;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;

public class DuplicateTriples {

    public static void main(String[] args){

        System.out.println(duplicateTriplesCase1("./resources/sample_map.ttl"));
    }
    public static boolean duplicateTriples1(String URI){
        //    rr:predicateObjectMap [
        //        rr:predicate ex:name;
        //        rr:objectMap [ rr:column "ENAME" ];
        //    ].
        String query = "\n" +
                "\n" +
                "SELECT (COUNT(?predicateObjectMap) AS ?count) ?predicate ?column ?template ?datatype ?termType\n" +
                "WHERE {\n" +
                "  ?subject \t<http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "}\n" +
                "GROUP BY ?predicate ?column ?template ?datatype ?termType\n" +
                "HAVING (COUNT(?predicateObjectMap)  > 1)\n" +
                "\n" +
                "\n";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");

        String predicate =  SPARQL.getStringVariable(ResultSetFactory.copyResults(results), "?predicate");
        String column =  SPARQL.getStringVariable(ResultSetFactory.copyResults(results), "?column");
        String datatype =  SPARQL.getStringVariable(ResultSetFactory.copyResults(results), "?datatype");
        String termtype =  SPARQL.getStringVariable(ResultSetFactory.copyResults(results), "?termType");
        System.out.println(predicate);
        System.out.println(column);
        System.out.println(datatype);
        System.out.println(termtype);

        return !count.isEmpty();
    }

    public static boolean removeDuplicates(String URI, ResultSet results ){
        return true;
    }

    public static boolean duplicateTriplesCase2(String URI){
        // rr:predicateMap [ rr:constant rdf:type ];
        // rr:objectMap [ rr:constant ex:Employee ]
        String query = "\n" +
                "  SELECT   (COUNT(?objectMap) AS ?count) ?predicateMapConstant ?objectMapConstant\n" +
                "WHERE {\n" +
                "  ?subject <http://www.w3.org/ns/r2rml#subjectMap> ?subjectMap.\n" +
                "\t?subject\t    <http://www.w3.org/ns/r2rml#predicateMap> ?predicateMap.\n" +
                "   \t?subject      <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
                "   \t?predicateMap      <http://www.w3.org/ns/r2rml#constant> ?predicateMapConstant.\n" +
                "    ?objectMap      <http://www.w3.org/ns/r2rml#constant> ?objectMapConstant.\n" +
                "}\n" +
                "\n" +
                "GROUP BY  ?predicateMapConstant ?objectMapConstant ?objectMap\n" +
                "HAVING (COUNT(?objectMap) > 1)";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");
        return !count.isEmpty();
    }

}
