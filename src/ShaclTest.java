import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ValidationUtil;
import java.io.*; 

public class ShaclTest {

	public static void main(String[] args) throws InterruptedException {
		String original_mapping_file  = "./resources/sample_map.ttl";
		String function_file  = "./resources/function.ttl";
		String output_file = "./resources/output.ttl";
		String new_sample_map = "./resources/new_sample_map.ttl";
		copySampleMap(original_mapping_file, new_sample_map);
		runTest(function_file, original_mapping_file, output_file);
		//Thread.sleep(10000);
		runTest(function_file, new_sample_map, output_file);

		
	}

	public static void parseResults(){

	}



	public static void runTest(String function_file,String data_file, String output_file) {
		Model data = ModelFactory.createDefaultModel();
		data.read(data_file);
		Model function = ModelFactory.createDefaultModel();
		function.read(function_file);
		Resource report = ValidationUtil.validateModel(data, function, true);
		report.getModel().write(System.out, "TURTLE");
		File file = new File(output_file);
		try {

			FileOutputStream fop = new FileOutputStream(file);
			// fop.write(contentInBytes);
			report.getModel().write(fop, "TURTLE");
			fop.flush();
			fop.close();
			System.out.println("Validation report out written to " + output_file);

		} catch (Exception e) {

			System.out.println("File not found!");

		}


	}


	public static void copySampleMap(String input_file, String output_file){
		Model data = ModelFactory.createDefaultModel();
		data.read(input_file);


		File copy_file = new File(output_file);
		try {

			FileOutputStream copy_file_stream = new FileOutputStream(copy_file);
			// fop.write(contentInBytes);
			data.write(copy_file_stream, "TURTLE");
			copy_file_stream.flush();
			copy_file_stream.close();
			System.out.println("Sample map copied to " + output_file);

		} catch (Exception e) {

			System.out.println("File not found!");

		}

	}

}
