package JavaClasses;

import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.*;
import java.io.File;
import java.io.FileOutputStream;

public class SPARQL {

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#" ;
    public static String FOAF_NS = "http://xmlns.com/foaf/0.1/";


    public static void main(String[] args){
        String query2 = "\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "\n" +
                "#\n" +
                "DELETE {\n" +
                "\t?subjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate\n" +
                "}\n" +
                "WHERE{\n" +
                "  SELECT ?predicate ?subject ?subjectMap WHERE{\n" +
                "?subject <http://www.w3.org/ns/r2rml#subjectMap> ?subjectMap;\n" +
                "           <http://www.w3.org/ns/r2rml#predicateObjectMap> ?objectMap.\n" +
                "  ?objectMap \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?objectMap <http://www.w3.org/ns/r2rml#objectMap> ?object.\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "\n" +
                "    } \n" +
                "GROUP BY ?predicate ?column ?template ?datatype ?termType ?subject ?subjectMap\n" +
                "HAVING (COUNT(?objectMap) > 1)\n" +
                "}\n";
        String query = "\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "\n" +
                "#\n" +
                "\n" +
                "DELETE {\n" +
                " ?o \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  \n" +
                "}\n" +
                "WHERE{\n" +
                "    SELECT ?subject ?o ?predicate WHERE{\n" +
                "  ?subject <http://www.w3.org/ns/r2rml#predicateObjectMap> ?o.\n" +
                "  ?o \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  {\n" +
                "  SELECT ?predicate   WHERE{\n" +
                "?subject <http://www.w3.org/ns/r2rml#subjectMap> ?subjectMap;\n" +
                "           <http://www.w3.org/ns/r2rml#predicateObjectMap> ?objectMap.\n" +
                "  ?objectMap \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?objectMap <http://www.w3.org/ns/r2rml#objectMap> ?object.\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "\n" +
                "    } \n" +
                "GROUP BY ?predicate ?column ?template ?datatype ?termType\n" +
                "HAVING (COUNT(?objectMap) > 1) LIMIT 1\n" +
                "  }\n" +
                "}\n" +
                "}\n" +
                "  \n" +
                "\n" +
                "\n";
        String q = "\n" +
                "\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "\n" +
                "#\n" +
                "\n" +
                "DELETE {\n" +
                " ?o \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  \n" +
                "}\n" +
                "WHERE{\n" +
                "    SELECT ?subject ?o ?predicate WHERE{\n" +
                "  ?subject <http://www.w3.org/ns/r2rml#predicateObjectMap> ?o.\n" +
                "  ?o \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  {\n" +
                "  SELECT ?predicate   WHERE{\n" +
                "?subject <http://www.w3.org/ns/r2rml#subjectMap> ?subjectMap;\n" +
                "           <http://www.w3.org/ns/r2rml#predicateObjectMap> ?objectMap.\n" +
                "  ?objectMap \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?objectMap <http://www.w3.org/ns/r2rml#objectMap> ?object.\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "\n" +
                "    } \n" +
                "GROUP BY ?predicate ?column ?template ?datatype ?termType\n" +
                "HAVING (COUNT(?objectMap) > 1)\n" +
                "  }\n" +
                "}\n" +
                "  LIMIT 1\n" +
                "}\n" +
                "  \n" +
                "\n" +
                "\n";
        String j = "\n" +
                "\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "\n" +
                "#\n" +
                "\n" +
                "DELETE {\n" +
                " ?subject <http://www.w3.org/ns/r2rml#predicateObjectMap> ?o." +
                " ?o <http://www.w3.org/ns/r2rml#predicate> ?predicate. \n" +
                " ?o <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
                "  ?objectMap \t<http://www.w3.org/ns/r2rml#column> ?column." +
                " ?objectMap \t<http://www.w3.org/ns/r2rml#template> ?template." +
                "?objectMap \t<http://www.w3.org/ns/r2rml#datatype> ?datatype." +
                " ?objectMap \t<http://www.w3.org/ns/r2rml#termType> ?termType." +
                "}\n" +
                "WHERE{\n" +
                "    SELECT ?subject ?o ?predicate ?objectMap ?column ?datatype ?termType ?template WHERE{\n" +
                "  ?subject <http://www.w3.org/ns/r2rml#predicateObjectMap> ?o.\n" +
                "  ?o \t<http://www.w3.org/ns/r2rml#predicate> ?predicate." +
                "  ?o <http://www.w3.org/ns/r2rml#objectMap> ?objectMap.   \n" +
                "  OPTIONAL {?objectMap \t<http://www.w3.org/ns/r2rml#column> ?column. }" +
                " OPTIONAL {?objectMap \t<http://www.w3.org/ns/r2rml#template> ?template. }" +
                " OPTIONAL {?objectMap \t<http://www.w3.org/ns/r2rml#datatype> ?datatype. }" +
                "  OPTIONAL {?objectMap \t<http://www.w3.org/ns/r2rml#termType> ?termType. }" +
                "{\n" +
                "  SELECT  ?predicate ?column ?template ?datatype ?termType  WHERE{\n" +
                "?subject <http://www.w3.org/ns/r2rml#subjectMap> ?subjectMap;\n" +
                "           <http://www.w3.org/ns/r2rml#predicateObjectMap> ?objectMap.\n" +
                "  ?objectMap \t<http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?objectMap <http://www.w3.org/ns/r2rml#objectMap> ?object.\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#column> ?column. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#template> ?template. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#datatype> ?datatype. }\n" +
                "  OPTIONAL {?object \t<http://www.w3.org/ns/r2rml#termType> ?termType. }\n" +
                "\n" +
                "    } \n" +
                "GROUP BY ?predicate ?column ?template ?datatype ?termType\n" +
                "HAVING (COUNT(?objectMap) > 1)\n" +
                "  }\n" +
                "}\n" +
                "  LIMIT 1\n" +
                "}\n" +
                "  \n" +
                "\n" +
                "\n";
        String l = "\n" +
                "DELETE {\n" +
                "    ?subject \t<http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap. \n" +
                "  \n" +
                "}\n" +
                "WHERE{\n" +
                "SELECT  ?predicateObjectMap ?predicate ?column ?template ?datatype ?termType\n" +
                "WHERE{\n" +
                "  ?subject \t<http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#predicate> ?predicate.\n" +
                "  ?predicateObjectMap <http://www.w3.org/ns/r2rml#objectMap> ?objectMap.   {\n" +
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
                "}\n" +
                "  }}";
        boolean result1 = DuplicateTriples.duplicateTriples("./resources/sample_map.ttl");
        updateData(l, "./resources/sample_map.ttl", "./resources/test.ttl");
        boolean result2 = DuplicateTriples.duplicateTriples("./resources/test.ttl");
        System.out.println(result1);
        System.out.println(result2);
        //   System.out.println(duplicateTriples("./resources/sample_map.ttl"));
       // System.out.println(j);
        // String query = String.format("ASK {<%s> <%s> ?label }",FOAF_NS+"age", RDFS_NS+"label");
        // System.out.println(SPARQL.askQuery( FOAF_NS+"Person", query));
//        String query = "\n" +
//                "SELECT ?subject1 ?subject2 ?column1 ?column2 ?datatype1 ?datatype2\n" +
//                "WHERE {\n" +
//                "  \t?subject1 <http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap1.\n" +
//                "    ?subject2 <http://www.w3.org/ns/r2rml#predicateObjectMap> ?predicateObjectMap2.\n" +
//                "    ?predicateObjectMap1 <http://www.w3.org/ns/r2rml#predicate> ?predicateMap1.\n" +
//                "    ?predicateObjectMap2 <http://www.w3.org/ns/r2rml#predicate> ?predicateMap1.\n" +
//                "  \t?predicateObjectMap1 <http://www.w3.org/ns/r2rml#objectMap> ?objectMap1. \n" +
//                "    ?predicateObjectMap2 <http://www.w3.org/ns/r2rml#objectMap> ?objectMap2. \n" +
//                "  \t?objectMap1 <http://www.w3.org/ns/r2rml#column> ?column1. \n" +
//                "    ?objectMap2 <http://www.w3.org/ns/r2rml#column> ?column2.  \n" +
//                "    \t?objectMap1 <http://www.w3.org/ns/r2rml#datatype> ?datatype1. \n" +
//                "    ?objectMap2 <http://www.w3.org/ns/r2rml#datatype> ?datatype2.  \n" +
//                "  FILTER(str(?column1) != str(?column2)) #&& str(?datatype1) != str(?datatype2))\n" +
//                "}";



    }



    public static boolean askQuery(String URI, String query){
        try {
            Model model = ModelFactory.createDefaultModel().read(URI);
            Query askQuery = QueryFactory.create(String.format(query));
            QueryExecution qexec = QueryExecutionFactory.create(askQuery, model) ;
            boolean result = qexec.execAsk() ;
            qexec.close() ;
            return result;
        }
        catch(Exception e){
            System.out.println("ASK QUERY ERROR " + e);
            return false;
        }


    }
    public static void updateData(String updateQuery, String inputFile, String outputFile){
        System.out.println("UPDATE QUERY " + updateQuery);
        Model model = ModelFactory.createDefaultModel();
        model.read(inputFile);
        UpdateAction.parseExecute(updateQuery, model );
        model.write( System.out, "TURTLE" );
        File file = new File(outputFile);
        try{

            FileOutputStream fop = new FileOutputStream(file);
            // fop.write(contentInBytes);
            model.write(fop, "TURTLE");
            fop.flush();
            fop.close();
            String result = "UPDATE QUERY written to " + outputFile;
            System.out.println(result);


        }

        catch(Exception e) {
            String result = "File not found!";
            System.out.println(result);
        }

    }

    public static ResultSet selectQuery(String URI, String queryString){
        try{
            Model model = ModelFactory.createDefaultModel() ;
            model.read(URI);
            Query query = QueryFactory.create(queryString) ;
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                return results ;    // Passes the result set out of the try-resources
            }

        }
        catch (Exception e){
            System.out.println("SELECT query error "  + e);
            return null;
        }
    }

    public static String getStringVariable(ResultSet results, String variableName){
        try {
            for(;results.hasNext();){
                QuerySolution soln = results.nextSolution();
                System.out.println(soln);
                String s = soln.get(variableName).toString();
                // System.out.println(variableName);
                // System.out.println(soln.get("?predicate").toString());
                return s;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return "";

    }
}
