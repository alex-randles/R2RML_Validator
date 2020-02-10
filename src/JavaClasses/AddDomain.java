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


public class AddDomain{


   public static String AddDomainTriple(String DomainURI, String MappingFile){
		Model model = ModelFactory.createDefaultModel();
		model.read(MappingFile);

        model.write( System.out, "TURTLE" );

        String rename = String.format("INSERT  { ?o <http://www.w3.org/ns/r2rml#class>  <%s>}\n  WHERE {?t 	<http://www.w3.org/ns/r2rml#subjectMap> ?o}", DomainURI) ;
        System.out.println("SPARQL QUERY " + DomainURI);
        UpdateAction.parseExecute( rename, model );

        model.write( System.out, "TURTLE" );
		String output_file = "./resources/new_sample_map.ttl";
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