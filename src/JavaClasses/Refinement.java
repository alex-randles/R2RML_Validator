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

    public static void findValidDomain(String URI) {
        String queryString = String.format("" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "SELECT ?domain WHERE {<%s> rdfs:domain ?domain }", URI);
        ResultSet results = SPARQL.selectQuery(URI, queryString);
        String domain = SPARQL.getStringVariable(results, "?domain");
        if(!domain.isEmpty()){
                AddDomainTriple(domain, FileNames.refinedMappingFile);
        }
    }

    public static void AddDomainTriple(String DomainURI, String MappingFile){
        String rename = String.format("" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
                "INSERT  { ?object  rr:class <%s>}\n  WHERE {  ?subject rr:subjectMap ?object. }", DomainURI) ;
        SPARQL.updateData(rename, FileNames.refinedMappingFile, FileNames.refinedMappingFile);
    }

    public static boolean addDataTypeTriple(String dataTypeURI, String predicateURI, String MappingFile){
        Model model = ModelFactory.createDefaultModel().read(FileNames.refinedMappingFile);
        String updateQuery = String.format("" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
               "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
               "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
               "INSERT {  ?objectMap rr:datatype <%s> }\n" +
               "WHERE\n" +
               "  {  \n" +
               "  ?subject rr:predicateObjectMap ?predicateObjectMap.\n" +
               "  ?predicateObjectMap rr:predicate <%s>.\n" +
               "  ?predicateObjectMap rr:objectMap ?objectMap.\n" +
               "  \n" +
               "  } ",  dataTypeURI, predicateURI);
       SPARQL.updateData(updateQuery, FileNames.refinedMappingFile, FileNames.refinedMappingFile);
       return  true;
    }

    public static boolean changeDataTypeTriple(String correctDatatype, String incorrectDatatype, String predicateURI, String MappingFile){
        Model model = ModelFactory.createDefaultModel().read(FileNames.refinedMappingFile);
        String updateQuery = String.format("" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
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
                "  } ",  correctDatatype, predicateURI, incorrectDatatype);
        SPARQL.updateData(updateQuery, FileNames.refinedMappingFile, FileNames.refinedMappingFile);
        return  true;
    }

    public static boolean changeTermType(String predicateURI, String termType){
        String updateQuery = String.format("" +
                "PREFIX rr: <http://www.w3.org/ns/r2rml#>\n" +
                "DELETE {  ?objectMap rr:termType ?termType}\n" +
                "INSERT {  ?objectMap rr:termType \t<%s> }\n" +
                "WHERE\n" +
                "  {  \n" +
                "  ?subject rr:predicateObjectMap ?predicateObjectMap.\n" +
                "  ?predicateObjectMap rr:predicate <%s>.\n" +
                "  ?predicateObjectMap rr:objectMap ?objectMap.\n" +
                "  ?objectMap rr:termType ?termType.\n" +
                "  } ", termType, predicateURI);
        SPARQL.updateData(updateQuery, FileNames.refinedMappingFile, FileNames.refinedMappingFile);
        return true;
    }
}

