package JavaClasses;

import java.net.*;
import java.io.*;


import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;

import javax.xml.transform.Result;
import java.io.*;

public class DereferenceURI {

    public static void main(String[] args) {
        // main method for testing purposes
//        String[] s = {"http://xmlns.com/foaf/0.1/Project", "http://xmlns.com/foaf/0.1/Person"};
//        System.out.println(validateDisjointClasses(s));
        boolean  s = validateRange("http://dbpedia.org/ontology/areaCode","http://www.w3.org/2001/XMLSchema#g");
        System.out.println(s);
    }

    public static boolean getResponseCode(String string_URL) {
        try {
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(string_URL + " produces error code " + code);
            if (code == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return true;
        }

    }


    public static void findValidDomain(String URI) {

        System.out.println("FINDING DOMAIN");
        String queryString = String.format("SELECT ?domain WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> ?domain }", URI);
        ResultSet results = SPARQL.selectQuery(URI, queryString);
         for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                String domain = soln.get("?domain").toString() ;   // Get a result variable - must be a literal
                if(!domain.isEmpty()){
                     AddDomain.AddDomainTriple(domain, "./resources/sample_map.ttl");
                     System.out.println("ADDING DOMAIN " + domain);
                     return;

                }

            }



//        StmtIterator iter = data.listStatements();
//        System.out.println(iter);
//        while (iter.hasNext()) {
//            Statement stmt = iter.nextStatement();  // get next statement
//            Resource subject = stmt.getSubject();     // get the subject
//            Property predicate = stmt.getPredicate();   // get the predicate
//            RDFNode object = stmt.getObject();      // get the object
//            if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && subject.toString().equals(URI)) {
//                String result = AddDomain.AddDomainTriple(object.toString(), "./resources/sample_map.ttl");
//                System.out.println("ADDING DOMAIN " + object.toString());
//                System.out.println(result);
//            }
//
//        }
    }

    public static boolean validateAllDomains(String[] mappingSubject, String mappingPredicate) {
        if (mappingSubject.length == 0) {
            System.out.println("NO DOMAIN CLASSES");
            findValidDomain(mappingPredicate);
            return true;
        }
        for (String subject : mappingSubject) {
            if (validateDomain((subject), mappingPredicate) == true) {
                return true;
            }
        }
        return false;

    }

    public static boolean validateDomain(String mappingSubject, String mappingPredicate) {
        try {
            System.out.println("CHECKING DOMAIN");
            Model data = ModelFactory.createDefaultModel();
            data.read(mappingSubject);
            System.out.println(mappingPredicate);
//
//
//                StmtIterator iter = data.listStatements();
//                // print out the predicate, subject and object of each statement
//                int i = 0;
//                boolean inDomain = false;
//
//                while (iter.hasNext()) {
//                    Statement stmt      = iter.nextStatement();  // get next statement
//                    Resource  subject   = stmt.getSubject();     // get the subject
//                    Property  predicate = stmt.getPredicate();   // get the predicate
//                    RDFNode   object    = stmt.getObject();      // get the object
//                    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && object.toString().equals(mappingSubject) && !(subject.toString().equals(mappingPredicate))){
//                       System.out.println("ENTERING DOMAIN IF STATEMENT");
//                       System.out.println("SUBJECT " + subject.toString());
//                       System.out.println("OBJECT " + object.toString());
//                    System.out.println("PREDICATE " + predicate.toString());
//                        System.out.println(mappingPredicate + " is not in the the domain of:  " + mappingSubject );
//                }
//                else if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && object.toString().equals(mappingSubject) && subject.toString().equals(mappingPredicate)){
//                    inDomain = true;
//                }


        boolean inDomain = SPARQL.askQuery(mappingSubject, String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingSubject, mappingPredicate));
        String s = String.format("ASK {<%s> <http://www.w3.org/2000/01/rdf-schema#domain> <%s> }", mappingPredicate, mappingSubject);
        System.out.println(s + inDomain);


        if (!inDomain) {
            findValidDomain(mappingPredicate);
        }
        return inDomain;


    }

        catch(Exception e)

        {
            findValidDomain(mappingPredicate);
            System.out.println(e + " ERROR ");
            return true;
        }
    }



   public static void fixDataType(String URI, String mappingFile) {
//            System.out.println("CHECKING DATATYPE OF"  + URI);
//        		Model data = ModelFactory.createDefaultModel();
//		data.read(URI);
//
//
//StmtIterator iter = data.listStatements();
//while (iter.hasNext()) {
//    Statement stmt      = iter.nextStatement();  // get next statement
//    Resource  subject   = stmt.getSubject();     // get the subject
//    Property  predicate = stmt.getPredicate();   // get the predicate
//    RDFNode   object    = stmt.getObject();      // get the object
////    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#range") && subject.toString().equals(URI)){
////       System.out.println("ENTERING DATATYPE IF STATEMENT");
////    String validDataType = object.toString();
////    String[] parts = validDataType.split("#");
////    if (parts[0].equals("http://www.w3.org/2001/XMLSchema")){
////         System.out.println("ADDING DATATYPE " + validDataType + " subject " + subject.toString());
//        AddDataType.AddDataTypeTriple(subject.toString(),validDataType, mappingFile);
//       //System.exit(0
       String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range}", URI);
       ResultSet results = SPARQL.selectQuery(URI, query);
       for (; results.hasNext(); ) {
           QuerySolution soln = results.nextSolution();
           String range = soln.get("?range").toString();   // Get a result variable - must be a literal
           if (!range.isEmpty()) {
               System.out.println("ADDING RANGE " + range);
               String[] parts = range.split("#");
               if (parts[0].equals("http://www.w3.org/2001/XMLSchema")) {
                   System.out.println("ADDING DATATYPE " + range + " subject " + URI.toString());
                   AddDataType.AddDataTypeTriple(URI,range, mappingFile);
                   return;

               }
           }

       }
   }

        // return;
 //   }

//}








   public static boolean checkRDF(String uri){
        try{

            Model data = ModelFactory.createDefaultModel();
            data.read(uri,"RDF/XML");

            StmtIterator iter = data.listStatements();
            if (iter.hasNext()) {
                return true;

                }
            else{
                return false;
            }
            }



        catch(Exception e){
            System.out.println("Error fetching RDF " + e);
            return false;
        }
        }


 public static boolean validateRange(String predicateURI, String dataTypeURI){
        try{
//
//        	Model data = ModelFactory.createDefaultModel();
//		    data.read(predicateURI,"RDF/XML");
//            StmtIterator iter = data.listStatements();
//            // print out the predicate, subject and object of each statement
//            int i = 0;
//            while (iter.hasNext()) {
//                Statement stmt      = iter.nextStatement();  // get next statement
//                Resource  subject   = stmt.getSubject();     // get the subject
//                Property  predicate = stmt.getPredicate();   // get the predicate
//                RDFNode   object    = stmt.getObject();      // get the object
//
//                // System.out.println(predicate.toString() + " " + object.toString() + " " +  subject.toString());
//                if (subject.toString().equals(predicateURI) && predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#range") && !(object.toString().equals(dataTypeURI))){
//                        System.out.println(object.toString() +  dataTypeURI);
//                        System.out.println(object.toString().equals(dataTypeURI));
//                        System.out.println( object.toString() + " is not in range of " + subject.toString()) ;;
//                        System.out.println("range is " + object.toString());
//                        return false;
//
//                }
            String query = String.format("SELECT ?range WHERE {<%s> <http://www.w3.org/2000/01/rdf-schema#range> ?range} ", predicateURI);
            ResultSet results = SPARQL.selectQuery(predicateURI, query);
            System.out.println("tets");
            System.out.println(results);
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                String range = soln.get("?range").toString();   // Get a result variable - must be a literal
                if (!range.isEmpty()) {
                    String[] parts = range.split("#");
                    if (parts[0].equals("http://www.w3.org/2001/XMLSchema")) {
                        if(!range.equals(dataTypeURI)){
                            return false;
                        }

                    }
                }
            }
            return true;
        }
        catch(Exception e){
            System.out.println(e + "validating range");
        }
        return true;
        }



    public static boolean validateDisjointClasses(String[] classesURI){
     try{

         for (String currentClassURI : classesURI){

             for (String comparisonURI : classesURI) {
                 String query = String.format("ASK {<%s> <http://www.w3.org/2002/07/owl#disjointWith> <%s> } ", comparisonURI, currentClassURI);
                 boolean result = SPARQL.askQuery(comparisonURI, query);
                 System.out.println(query);
                 String s = String.format("%s result", result);
                 System.out.println(s);
                 if (result==true) {
                     return true;
                 }

             }

//        for (String currentClassURI : classesURI){
//            Model data = ModelFactory.createDefaultModel();
//		    data.read(currentClassURI);
//		    StmtIterator iter = data.listStatements();
//            // print out the predicate, subject and object of each statement
//            while (iter.hasNext()) {
//                Statement stmt      = iter.nextStatement();  // get next statement
//                Resource  subject   = stmt.getSubject();     // get the subject
//                Property  predicate = stmt.getPredicate();   // get the predicate
//                RDFNode   object    = stmt.getObject();      // get the object
//
//            for (String comparisonURI : classesURI){
//                String query = String.format("ASK {<%s> <http://www.w3.org/2002/07/owl#disjointWith> <%s> } ", comparisonURI, currentClassURI)
//                boolean result = SPARQL.askQuery(comparisonURI, query);
//                if (result){
//                    return result;
//                }
//                if (predicate.toString().equals("http://www.w3.org/2002/07/owl#disjointWith") && subject.toString().equals(comparisonURI) && object.toString().equals(currentClassURI)){
//                        System.out.println(subject.toString() + " " + predicate.toString()  + " " + object.toString() );
//                        System.out.println("DISJOINT");
//                        return false;
//                }
//
//
//
//            }



//                    }
                    }
}
        catch(Exception e){
            System.out.println(e + "3474374747");
        }
        return false;

        }
}