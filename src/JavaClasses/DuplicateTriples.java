package JavaClasses;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;

public class DuplicateTriples {

    public static String mappingFile = "./resources/sample_map.ttl";
    public static void main(String[] args){
        detectDuplicateTriples();
    }

    public static boolean detectDuplicateTriples(){
        if(duplicateTriples1(mappingFile)){
            return true;
        }
        return false;
    }

    public static boolean duplicateTriples1(String URI){
        //    rr:predicateObjectMap [
        //        rr:predicate ex:name;
        //        rr:objectMap [ rr:column "ENAME" ];
        //    ].

//        String saveDuplicatePredicateObjectMap = "\n" +
//                "  SELECT ?subject ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap ?child ?parent \n" +
//                "  WHERE{\n" +
//                "      ?subject      <http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
//                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
//                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#column> ?column. }\n" +
//                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#template> ?template. }\n" +
//                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#language> ?language. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#parentTriplesMap> ?parentTriplesMap. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#joinCondition> ?joinCondition. }\n" +
//                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#child> ?child. }  \n" +
//                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#parent> ?parent. }{\n" +
//                "SELECT ?subject ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap ?child ?parent \n" +
//                "WHERE {\n" +
//                "  ?subject      <http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
//                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
//                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#column> ?column. }\n" +
//                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#template> ?template. }\n" +
//                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#language> ?language. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#parentTriplesMap> ?parentTriplesMap. }\n" +
//                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#joinCondition> ?joinCondition. }\n" +
//                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#child> ?child. }  \n" +
//                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#parent> ?parent. }\n" +
//                "}\n" +
//                "GROUP BY ?subject ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap ?child ?parent \n" +
//                "HAVING (COUNT(?predicateObjectMap)  > 1)\n" +
//                "    }\n" +
//                "}\n" +
//                "  GROUP BY  ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap ?child ?parent ?subject \n" +
//                "\n";
        String query = "\n" +
                "\n" +
                "SELECT (COUNT(?predicateObjectMap) AS ?count) ?subject ?predicate ?column ?template ?datatype ?termType ?language\n" +
                "WHERE {\n" +
                "  ?subject \t<http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?objectMap    <http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#language> ?language. }\n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#parentTriplesMap> ?parentTriplesMap. }\n" +
                "  OPTIONAL {?objectMap     <http://www.w3.org/ns/r2rml#joinCondition> ?joinCondition. }\n" +
                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#child> ?child. }" +
                "  OPTIONAL {?joinCondition     <http://www.w3.org/ns/r2rml#parent> ?parent. }" +
                "}\n" +
                "GROUP BY ?subject ?predicate ?column ?template ?datatype ?termType ?language ?parentTriplesMap ?joinCondition ?child ?parent\n" +
                "HAVING (COUNT(?predicateObjectMap)  > 1)\n" +
                "\n" +
                "\n";
        ResultSet results = SPARQL.selectQuery(URI, query);
        String count = SPARQL.getStringVariable(results, "?count");
        System.out.println("duplicate triples count");
        System.out.println(query);
        System.out.println(!count.isEmpty());

        return count.isEmpty();
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
