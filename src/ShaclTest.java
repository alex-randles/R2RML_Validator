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
