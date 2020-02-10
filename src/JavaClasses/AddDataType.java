package JavaClasses;

import java.net.*;
import java.io.*;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.*;
import org.apache.jena.query.* ;
import java. util.*;
import java.io.*;
import org.apache.jena.update.*;
import org.apache.jena.update.UpdateRequest;


public class AddDataType{

    public static void main(String[] args){
        AddDataTypeTriple("xsddss", " http://www.w3.org/2001/XMLSchema#string", "./resources/sample_map.ttl");

    }
   public static String AddDataTypeTriple(String dataTypeURI, String predicateURI, String MappingFile){
		Model model = ModelFactory.createDefaultModel();
		System.out.println("CHECKING DATATYPE FOR "+ dataTypeURI + MappingFile);
		model.read(MappingFile);

        // model.write( System.out, "TURTLE" );

        String updateDataType = String.format("INSERT  { ?object  <http://www.w3.org/ns/r2rml#datatype>  <%s>}\n  WHERE { ?subject <http://www.w3.org/ns/r2rml#predicate> <%s>; <http://www.w3.org/ns/r2rml#objectMap> ?object. }", predicateURI, dataTypeURI) ;
        System.out.println("SPARQL QUERY " + updateDataType);
        UpdateAction.parseExecute( updateDataType, model );

        model.write( System.out, "TURTLE" );
		String output_file = "./resources/new_datatype_sample_map.ttl";
		File file = new File(output_file);
        try{

			FileOutputStream fop = new FileOutputStream(file);
			// fop.write(contentInBytes);
			model.write(fop, "TURTLE");
			fop.flush();
			fop.close();
			String result = "Validation report out written to " + output_file;
			// System.out.println(result);
			return result;

		}

		catch(Exception e) {
	        String result = "File not found!";
			// System.out.println(result);
			return result;
}


   }


  }