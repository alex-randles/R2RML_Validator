package JavaClasses;

import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.Update;
import org.apache.jena.query.* ;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;


public class TermType {
    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    public static String RR_NS = "http://www.w3.org/ns/r2rml#";

    public static void main(String[] args){
        // updateTermType( "http://www.w3.org/ns/r2rml#Literal", "http://xmlns.com/foaf/0.1/age");
        repairTermType( "http://xmlns.com/foaf/0.1/age");
    }
    public static void repairTermType(String URI){
//        System.out.println("REPARINING TERM TYPE FOR " + URI);
//        Model data = ModelFactory.createDefaultModel();
//        data.read(URI);
//        StmtIterator iter = data.listStatements();
//        while (iter.hasNext()) {
//            Statement stmt      = iter.nextStatement();  // get next statement
//            Resource subject   = stmt.getSubject();     // get the subject
//            Property  predicate = stmt.getPredicate();   // get the predicate
//            RDFNode   object    = stmt.getObject();      // get the object
//            Output.printTriples(subject.toString(), predicate.toString(), object.toString());
//
//            if (predicate.toString().equals(RDFS_NS + "range") && subject.toString().equals(URI) && object.toString().equals(RDFS_NS+"Literal")){
//                // Output.printTriples(subject.toString(), predicate.toString(), object.toString());
//                System.out.println(URI + "has range " + object.toString());
//                // updateTermType(RR_NS+"Literal", URI);
//            }
//
//        }

        System.out.println("REPARINING TERM TYPE FOR " + URI);
        // Model data = ModelFactory.createDefaultModel().read(URI);
        String literalQuery = String.format("ASK {<%s> <%s> <%s>}", URI, RDFS_NS+"range", RDFS_NS+"Literal");
        String dataTypeQuery = String.format("ASK\n" +
                "WHERE {\n" +
                "  <%s> <%s> ?label. \n" +
                "  FILTER( strStarts( str(?label), \"http://www.w3.org/2001/XMLSchema#\" ) ) .\n" +
                "}", URI, RDFS_NS+"range");
        boolean hasDataType = SPARQL.askQuery(URI,dataTypeQuery);
        boolean hasLiteral = SPARQL.askQuery(URI, literalQuery);
        if (hasDataType || hasLiteral){
            System.out.println(URI + " has datatype or literal");
            updateTermType(RR_NS+"Literal", URI);
        }


     }

    public static void updateTermType(String newTermType, String URI){
        System.out.println("UPDATING TERM TYPE");
        Model data = ModelFactory.createDefaultModel();
        data.read("./resources/new_sample_map.ttl");
        String selectObjectMap = String.format("SELECT ?oldTermType WHERE { ?objectMapSubject <http://www.w3.org/ns/r2rml#predicate> <%s>; \n" +
                "<http://www.w3.org/ns/r2rml#objectMap> ?objectMapValue.\n" +
                " ?objectMapValue <http://www.w3.org/ns/r2rml#termType> ?oldTermType } ", URI);
        Query query = QueryFactory.create(selectObjectMap) ;
        List<String> oldTermTypes = new ArrayList<String>();
        try (QueryExecution qexec = QueryExecutionFactory.create(query, data)) {
            ResultSet results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                RDFNode x = soln.get("varName") ;       // Get a result variable by name.
                try{
                    Resource termType = soln.getResource("?oldTermType") ; // Get a result variable - must be a resource
                    String oldTermType = termType.toString();
                    if (oldTermType.equals(newTermType)){
                        return;
                    }
                    oldTermTypes.add(oldTermType);
                    System.out.println("REMOVE LATER " + oldTermType);

                }
                catch (Exception e){

                }
            }
        }
        String updateQuery = String.format("INSERT { ?objectMapValue <http://www.w3.org/ns/r2rml#termType> <%s>} WHERE { ?objectMapSubject <http://www.w3.org/ns/r2rml#predicate> <%s> ; <http://www.w3.org/ns/r2rml#objectMap> ?objectMapValue}",
                newTermType, URI);
        SPARQL.updateData(updateQuery, "./resources/new_sample_map.ttl", "./resources/new_sample_map.ttl");
        if(!(oldTermTypes.isEmpty())) {
            for (String s: oldTermTypes){
                    System.out.println("DELETING TERM TYPE " + s);
                    String deleteQuery = String.format("DELETE { ?objectMapObject \t<http://www.w3.org/ns/r2rml#termType> <%s>. } \n" +
                            "WHERE {\n" +
                            "  ?predicateSubject ?predicate <%s>.\n" +
                            "  ?predicateSubject <http://www.w3.org/ns/r2rml#objectMap> ?objectMapObject.\n" +
                            "  ?objectMapObject \t<http://www.w3.org/ns/r2rml#termType> ?termTypeObject.          \n" +
                            "}\n", s, URI);
                    SPARQL.updateData(deleteQuery, "./resources/new_sample_map.ttl", "./resources/new_sample_map.ttl");


            }


            }



        }


}
