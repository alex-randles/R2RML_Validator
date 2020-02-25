package JavaClasses;
import java.io.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.*;


public class AddDomain{


   public static String AddDomainTriple(String DomainURI, String MappingFile){
		Model model = ModelFactory.createDefaultModel();
		model.read("./resources/new_sample_map.ttl");

        model.write( System.out, "TURTLE" );

	   	String rename = String.format("INSERT  { ?object  <http://www.w3.org/ns/r2rml#class>  <%s>}\n  WHERE {  ?subject <http://www.w3.org/ns/r2rml#subjectMap> ?object. }", DomainURI) ;
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