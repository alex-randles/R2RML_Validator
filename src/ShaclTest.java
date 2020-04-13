import JavaClasses.FileNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.validation.ValidationUtil;
import java.io.*; 

public class ShaclTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			printHeaders();
			copyMapping(FileNames.originalMappingFile, FileNames.refinedMappingFile);
			runTest(FileNames.function_file, FileNames.originalMappingFile, FileNames.reportFile);
		}
		catch (Exception e){
			System.out.println(e);
			System.out.println("Problem assessing your mapping... ");
		}
	}

	public static void printHeaders(){
		String processingMessage = "Please wait a few seconds for your mapping to be assessed and refined..";
		System.out.println(StringUtils.repeat('*', 70));
		System.out.println(StringUtils.repeat('*', 70));
		System.out.println(processingMessage);
		System.out.println(StringUtils.repeat('*', 70));
		System.out.println(StringUtils.repeat('*', 70));
	}

	public static void runTest(String function_file, String data_file, String output_file) {
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
			String validationReportMessage = String.format("Validation report written to %s", output_file);
			String refinedMappingMessage = String.format("Refined mapping written to %s", FileNames.refinedMappingFile);
			System.out.println(StringUtils.repeat('*', 70));
			System.out.println(validationReportMessage);
			System.out.println(StringUtils.repeat('*', 70));
			System.out.println(refinedMappingMessage);
			System.out.println(StringUtils.repeat('*', 70));

		} catch (Exception e) {
			System.out.println("File not found!");
		}
	}


	public static void copyMapping(String input_file, String output_file){
		Model data = ModelFactory.createDefaultModel();
		data.read(input_file);
		File copy_file = new File(output_file);
		try {
			FileOutputStream copy_file_stream = new FileOutputStream(copy_file);
			data.write(copy_file_stream, "TURTLE");
			copy_file_stream.flush();
			copy_file_stream.close();

		} catch (Exception e) {
		}
	}

}
