package JavaClasses;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateAction;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

public class Refinement {

    public static void findValidDomain(String URI) {
        String queryString = String.format("SELECT ?domain WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", URI);
        ResultSet results = SPARQL.selectQuery(URI, queryString);
        for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String domain = soln.get("?domain").toString() ;   // Get a result variable - must be a literal
            if(!domain.isEmpty()){
                AddDomainTriple(domain, "./resources/sample_map.ttl");
                return;
            }
        }
    }

    public static String AddDomainTriple(String DomainURI, String MappingFile){
        Model model = ModelFactory.createDefaultModel();
        model.read("./resources/new_sample_map.ttl");
        String rename = String.format("INSERT  { ?object  <http://www.w3.org/ns/r2rml#class>  <%s>}\n  WHERE {  ?subject <http://www.w3.org/ns/r2rml#subjectMap> ?object. }", DomainURI) ;
        UpdateAction.parseExecute(rename,model);
        String output_file = "./resources/new_sample_map.ttl";
        File file = new File(output_file);
        try{
            FileOutputStream fop = new FileOutputStream(file);
            // fop.write(contentInBytes);
            model.write(fop, "TURTLE");
            fop.flush();
            fop.close();
            String result = "Validation report out written to " + output_file;
            return result;
        }

        catch(Exception e) {
            String result = "File not found!";
            return result;
        }
    }

    public static String AddDataTypeTriple(String dataTypeURI, String predicateURI, String MappingFile){
        Model model = ModelFactory.createDefaultModel();
        model.read("./resources/new_sample_map.ttl");
        String selectQuery = String.format("SELECT * WHERE { ?subject 	<http://www.w3.org/ns/r2rml#predicate> 	<%s>; <http://www.w3.org/ns/r2rml#objectMap> ?p . ?p <http://www.w3.org/ns/r2rml#datatype> ?o}", dataTypeURI) ;
        Query query = QueryFactory.create(selectQuery) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            Iterator<QuerySolution> results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.next() ;
            }
        }
        String deleteQuery = String.format("DELETE {?p <http://www.w3.org/ns/r2rml#datatype> ?o }  WHERE { ?subject 	<http://www.w3.org/ns/r2rml#predicate> 	<%s>; <http://www.w3.org/ns/r2rml#objectMap> ?p . ?p <http://www.w3.org/ns/r2rml#datatype> ?o}", dataTypeURI) ;
        UpdateAction.parseExecute( deleteQuery, model );
        String updateQuery = String.format("INSERT  { ?object  <http://www.w3.org/ns/r2rml#datatype>  <%s>}\n  WHERE { ?subject <http://www.w3.org/ns/r2rml#predicate> <%s>; <http://www.w3.org/ns/r2rml#objectMap> ?object }", predicateURI, dataTypeURI) ;
        UpdateAction.parseExecute( updateQuery, model );
        String output_file = "./resources/new_sample_map.ttl";
        File file = new File(output_file);
        try{
            FileOutputStream fop = new FileOutputStream(file);
            model.write(fop, "TURTLE");
            fop.flush();
            fop.close();
            String result = "Validation report out written to " + output_file;
            return result;
        }

        catch(Exception e) {
            String result = "File not found!";
            System.out.println("ADDING DATATYPE ERROR"  + e);
            return result;
        }
    }
}

