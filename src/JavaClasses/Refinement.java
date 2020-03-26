package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.resultset.rw.ResultsWriter;
import org.apache.jena.update.UpdateAction;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

public class Refinement {
    public static void main(String[] args){
        ResultSet  s = SPARQL.selectQuery("http://dbpedia.org/ontology/army", "SELECT ?datatype WHERE { <http://dbpedia.org/ontology/army> <http://www.w3.org/2000/01/rdf-schema#range> ?datatype}" );
        System.out.println(SPARQL.getStringVariable(s, "?datatype"));
    }
    public static void findValidDomain(String URI) {
        String queryString = String.format("SELECT ?domain WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", URI);
        ResultSet results = SPARQL.selectQuery(URI, queryString);
        for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String domain = soln.get("?domain").toString() ;   // Get a result variable - must be a literal
            System.out.println("domain for " + URI + " is " + domain);
            if(!domain.isEmpty()){
                AddDomainTriple(domain, "./resources/new_sample_map.ttl");
                return;
            }
        }
    }

    public static void AddDomainTriple(String DomainURI, String MappingFile){
        String rename = String.format("INSERT  { ?object  <http://www.w3.org/ns/r2rml#class>  <%s>}\n  WHERE {  ?subject <http://www.w3.org/ns/r2rml#subjectMap> ?object. }", DomainURI) ;
        SPARQL.updateData(rename, "./resources/new_sample_map.ttl", "./resources/new_sample_map.ttl");
    }

    public static String AddDataTypeTriple(String dataTypeURI, String predicateURI, String MappingFile){
        Model model = ModelFactory.createDefaultModel();
        String output_file = "./resources/new_sample_map.ttl";
        model.read(output_file);
        String updateQuery = String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
               "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
               "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
               "DELETE {  ?objectMap rr:datatype ?datatype}\n" +
               "INSERT {  ?objectMap rr:datatype <%s> }\n" +
               "WHERE\n" +
               "  {  \n" +
               "  ?subject rr:predicateObjectMap ?predicateObjectMap.\n" +
               "  ?predicateObjectMap rr:predicate <%s>.\n" +
               "  ?predicateObjectMap rr:objectMap ?objectMap.\n" +
               "  ?objectMap rr:datatype ?datatype.\n" +
               "  } ",  dataTypeURI, predicateURI);
       SPARQL.updateData(updateQuery, output_file, output_file);
//        File file = new File(output_file);
//        try{
//            FileOutputStream fop = new FileOutputStream(file);
//            model.write(fop, "TURTLE");
//            fop.flush();
//            fop.close();
//            String result = "Validation report out written to " + output_file;
//            return result;
//        }
//
//        catch(Exception e) {
//            String result = "File not found!";
//            System.out.println("ADDING DATATYPE ERROR"  + e);
//            return result;
//        }
        return "shdhd";
    }

    public static boolean removeDuplicates(String URI){
        String removeQuery = "\n" +
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
                " OFFSET 1" +
                "\n" +
                "}\n" +
                "  }}";
        System.out.println(removeQuery);
        return true;
//        SPARQL.updateData(removeQuery, URI, URI);
//        return true;
    }
}

