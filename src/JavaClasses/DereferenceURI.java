package JavaClasses;

import java.net.*;
import java.io.*;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.*;

import java.io.*;

public class DereferenceURI{

    public static void main(String[] args){
        // main method for testing purposes
        //  http://xmlns.com/foaf/0.1/Group http://xmlns.com/foaf/0.1/Group http://example.com/ns#Employee
String mappingPredicate = "http://xmlns.com/foaf/0.1/Group";
String mappingSubject  =  "http://xmlns.com/foaf/0.1/member";
String vocabularyURI = "http://dbpedia.org/ontology/numberOfDistrict";
       // getRDF("http://xmlns.com/foaf/0.1/knows", "http://xmlns.com/foaf/0.1/knows", "http://www.w3.org/1999/02/22-rdf-syntax-ns#List");
// validateRange("http://xmlns.com/foaf/0.1/knows",  "http://www.w3.org/2001/XMLSchema#integer");
String[] classesURI = {"http://xmlns.com/foaf/0.1/Document", "http://xmlns.com/foaf/0.1/Person"};
// validateDomain("http://xmlns.com/foaf/0.1/made","http://xmlns.com/foaf/0.1/made", "http://dbpedia.org/ontology/ArchitecturalStructure");
// validateDisjointClasses(classesURI);
// validateRange("http://xmlns.com/foaf/0.1/made", "http://www.w3.org/2002/07/owl#Thing");
// validateDomain("http://dbpedia.org/ontology/co","http://dbpedia.org/ontology/co", "http://dbpedia.org/ontology/Person");
  // fixDataType("http://dbpedia.org/ontology/areaCode");

    }

    public static boolean getResponseCode(String string_URL){
        try{
            URL url = new URL(string_URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(string_URL + " produces error code " + code);
            if (code == 200) {
                return true;
            }
            return false;
        }
        catch(Exception e){
            System.out.println(e);
            return true ;
        }

}


public static void findValidDomain(String URI){

System.out.println("FINDING DOMAIN");
        		Model data = ModelFactory.createDefaultModel();
		data.read(URI);


StmtIterator iter = data.listStatements();
System.out.println(iter);
while (iter.hasNext()) {
    Statement stmt      = iter.nextStatement();  // get next statement
    Resource  subject   = stmt.getSubject();     // get the subject
    Property  predicate = stmt.getPredicate();   // get the predicate
    RDFNode   object    = stmt.getObject();      // get the object
    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && subject.toString().equals(URI)){
        String result = AddDomain.AddDomainTriple(object.toString(),  "./resources/sample_map.ttl");
        System.out.println("ADDING DOMAIN " + object.toString());
        System.out.println(result);
}

}
}
public static boolean validateAllDomains(String[] mappingSubject, String mappingPredicate){
     for (String subject : mappingSubject){
        if (validateDomain((subject), mappingPredicate) == true){
            return true;
        }
     }
     return false;

}
    public static boolean validateDomain(String mappingSubject, String mappingPredicate){
        try{
        System.out.println("CHECKING DOMAIN");
        		Model data = ModelFactory.createDefaultModel();
		data.read(mappingSubject);
           System.out.println(mappingPredicate);


StmtIterator iter = data.listStatements();
// print out the predicate, subject and object of each statement
int i = 0;
boolean inDomain = false;
while (iter.hasNext()) {
    Statement stmt      = iter.nextStatement();  // get next statement
    Resource  subject   = stmt.getSubject();     // get the subject
    Property  predicate = stmt.getPredicate();   // get the predicate
    RDFNode   object    = stmt.getObject();      // get the object
//     System.out.println("SUBJECT " + subject.toString());
//     System.out.println("OBJECT " + object.toString());
//     System.out.println("PREDICATE " + predicate.toString());
//     System.out.println("COMPARING TO SUBJECT " + mappingSubject  + " PREDICATE " + mappingPredicate);#
    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && object.toString().equals(mappingSubject) && !(subject.toString().equals(mappingPredicate))){
       System.out.println("ENTERING DOMAIN IF STATEMENT");
       System.out.println("SUBJECT " + subject.toString());
    System.out.println("OBJECT " + object.toString());
    System.out.println("PREDICATE " + predicate.toString());
        System.out.println(mappingPredicate + " is not in the the domain of:  " + mappingSubject );
}
else if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#domain") && object.toString().equals(mappingSubject) && subject.toString().equals(mappingPredicate)){
    inDomain = true;
}



}
if (!inDomain){
findValidDomain(mappingPredicate);
}
return inDomain;
}



        catch(Exception e){
            System.out.println(e + " ERROR ");
            return true;
        }
        }

   public static void fixDataType(String URI, String mappingFile){
            System.out.println("CHECKING DATATYPE OF"  + URI);
        		Model data = ModelFactory.createDefaultModel();
		data.read(URI);


StmtIterator iter = data.listStatements();
while (iter.hasNext()) {
    Statement stmt      = iter.nextStatement();  // get next statement
    Resource  subject   = stmt.getSubject();     // get the subject
    Property  predicate = stmt.getPredicate();   // get the predicate
    RDFNode   object    = stmt.getObject();      // get the object
    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#range") && subject.toString().equals(URI)){
       System.out.println("ENTERING DATATYPE IF STATEMENT");
    String validDataType = object.toString();
    String[] parts = validDataType.split("#");
    if (parts[0].equals("http://www.w3.org/2001/XMLSchema")){
         System.out.println("ADDING DATATYPE " + validDataType + " subject " + subject.toString());
        AddDataType.AddDataTypeTriple(subject.toString(),validDataType, mappingFile);
       //  System.exit(0);

        // return;
    }

}




}


   }
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

        		Model data = ModelFactory.createDefaultModel();
		data.read(predicateURI,
           "RDF/XML");


StmtIterator iter = data.listStatements();

// print out the predicate, subject and object of each statement
int i = 0;
while (iter.hasNext()) {
    Statement stmt      = iter.nextStatement();  // get next statement
    Resource  subject   = stmt.getSubject();     // get the subject
    Property  predicate = stmt.getPredicate();   // get the predicate
    RDFNode   object    = stmt.getObject();      // get the object

// System.out.println(predicate.toString() + " " + object.toString() + " " +  subject.toString());
if (subject.toString().equals(predicateURI) && predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#range") && !(object.toString().equals(dataTypeURI))){
        System.out.println(object.toString() +  dataTypeURI);
        System.out.println(object.toString().equals(dataTypeURI));
        System.out.println( object.toString() + " is not in range of " + subject.toString()) ;;
        System.out.println("range is " + object.toString());
        return false;

}



        }
}
        catch(Exception e){
            System.out.println(e + "3474374747");
        }
        return true;
        }



    public static boolean validateDisjointClasses(String[] classesURI){
     try{
        for (String currentClassURI : classesURI){
            Model data = ModelFactory.createDefaultModel();
		    data.read(currentClassURI);
		    StmtIterator iter = data.listStatements();
            // print out the predicate, subject and object of each statement
            while (iter.hasNext()) {
                Statement stmt      = iter.nextStatement();  // get next statement
                Resource  subject   = stmt.getSubject();     // get the subject
                Property  predicate = stmt.getPredicate();   // get the predicate
                RDFNode   object    = stmt.getObject();      // get the object

            for (String comparisonURI : classesURI){
//                 System.out.println(comparisonURI +  " comparing with " + currentClassURI);
//                 if (predicate.toString().equals("http://www.w3.org/2002/07/owl#disjointWith")){
//                  System.out.println(subject.toString() + " " + predicate.toString()  + " " + object.toString());
//                 }
                if (predicate.toString().equals("http://www.w3.org/2002/07/owl#disjointWith") && subject.toString().equals(comparisonURI) && object.toString().equals(currentClassURI)){
                        System.out.println(subject.toString() + " " + predicate.toString()  + " " + object.toString() );
                        System.out.println("DISJOINT");
                        return false;
                }



            }

            // System.out.println(predicate.toString() + " " + object.toString() + " " +  subject.toString());
//                 if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#range") && !(object.toString().equals(objectURI)) && subject.toString().equals(predicateURI)){
//                     System.out.println(objectURI + " is not in the the range of:  " + predicateURI );
//                    return false;
//                 }
                // System.out.println("ddhdhdh");
                // System.out.println(" .");


                    }
                    }
}
        catch(Exception e){
            System.out.println(e + "3474374747");
        }
        return true;

        }
}