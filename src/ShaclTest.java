import JavaClasses.FileNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.validation.ValidationUtil;
import java.io.*; 

// not needed 
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class ShaclTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			printHeaders();
			FileUtils.cleanDirectory(new File("./cache"));
			String inputFile = args[0];
			copyMapping(inputFile, FileNames.originalMappingFile);
			copyMapping(FileNames.originalMappingFile, FileNames.refinedMappingFile);
			runTest(FileNames.shapesFile, FileNames.originalMappingFile, FileNames.reportFile);
		}
		catch (Exception e){
			System.out.println(e); 
			String outputMessage = String.format("Problem assessing mapping!!!\n" +
					"Please ensure '%s' contains a valid R2RML mapping.....", args[0]);
			System.out.println(outputMessage);
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
			String queryString = "SELECT ?focusNode WHERE {?s <http://www.w3.org/ns/shacl#focusNode> ?focusNode } "; 
			System.out.println(queryString); 
			QueryExecution qexec = QueryExecutionFactory.create(queryString, report.getModel());
			                ResultSet results = qexec.execSelect() ;
                results = ResultSetFactory.copyResults(results) ;
                qexec.close();
			            String stringVariable = results.nextSolution().get("?focusNode").toString();
			            System.out.println("ddhdhdhd");
			            System.out.println(stringVariable); 
			            			String query = String.format("PREFIX rr: <http://www.w3.org/ns/r2rml#>" +
											"SELECT ?s ?p ?o WHERE {_:%s rr:termType ?o   } ", stringVariable) ;
System.out.println(query); 
		QueryExecution qexec2 = QueryExecutionFactory.create(query, data);
			                ResultSet results2 = qexec2.execSelect() ;
                results2 = ResultSetFactory.copyResults(results2) ;
                qexec2.close();
			for ( ; results2.hasNext() ; )
			{
				QuerySolution soln = results2.nextSolution() ;
				System.out.print(soln.get("?s") );
				System.out.print("  "); 
								System.out.print(soln.get("?p") );
												System.out.print("  "); 

								System.out.println(soln.get("?o") );

			}




// testing above 
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
			System.out.println(e); 
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
