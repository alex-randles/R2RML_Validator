import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ValidationUtil;
import java.io.*; 

public class ShaclTest {

	public static void main(String[] args) {

		// The data
		Model data = ModelFactory.createDefaultModel();
		data.read("./resources/sample_map.ttl");



		String copy_mapping_file = "./resources/new_sample_map.ttl";
		File copy_file = new File(copy_mapping_file);
        try{

			FileOutputStream copy_file_stream = new FileOutputStream(copy_file);
			// fop.write(contentInBytes);
			data.write(copy_file_stream, "TURTLE");
			copy_file_stream.flush();
			copy_file_stream.close();
			System.out.println("Sample map copied to " + copy_mapping_file);

		}

		catch(Exception e) {

			System.out.println("File not found!");

		}




		// read function.ttl -- it creates a rule with JavaScript functionality to compute the
		// area of circles using its radius. But watch out, this code assume that all target nodes
		// hava a property radius. You need to add conditionals in the code
		Model function = ModelFactory.createDefaultModel();
		function.read("./resources/function.ttl");
		
		// You can add additional parameters, but you can consult the documentation
		// This model will only contain the inferred triples (via the rules)
		// Model inferenced = RuleUtil.executeRules(data, function, null , null);
		// Model inferenced = RuleUtil.executeRules(data, function, data , null);
		// inferenced.write(System.out, "TURTLE");



		// Output to file aswell 
		 
		Resource report = ValidationUtil.validateModel(data, function, true);
		report.getModel().write(System.out, "TURTLE");
	    Model new_data = ModelFactory.createDefaultModel();
		new_data.read("./resources/new_sample_map.ttl");

        // copy sample map over so not to change the manipulate the original






		// read function.ttl -- it creates a rule with JavaScript functionality to compute the
		// area of circles using its radius. But watch out, this code assume that all target nodes
		// hava a property radius. You need to add conditionals in the code
		Model new_function = ModelFactory.createDefaultModel();
		function.read("./resources/function.ttl");

		// You can add additional parameters, but you can consult the documentation
		// This model will only contain the inferred triples (via the rules)
		// Model inferenced = RuleUtil.executeRules(data, function, null , null);
		// Model inferenced = RuleUtil.executeRules(data, function, data , null);
		// inferenced.write(System.out, "TURTLE");



		// Output to file aswell

		Resource new_report = ValidationUtil.validateModel(new_data, new_function, true);
		new_report.getModel().write(System.out, "TURTLE");
		String output_file = "./resources/output.ttl";
		File file = new File(output_file);
        try{

			FileOutputStream fop = new FileOutputStream(file);
			// fop.write(contentInBytes);
			report.getModel().write(fop, "TURTLE");
			fop.flush();
			fop.close();
			System.out.println("Validation report out written to " + output_file);

		}

		catch(Exception e) {

			System.out.println("File not found!");

		}
		
	}

}
