var NS = "http://www.w3.org/ns/r2rml#";
var predicates = [];
var columns = [];
var groupings = [];
var no_duplicates = true;
var duplicates_checked = false;
var constant_template_checked = false;
var tables_count_check = false;
var mappings = {} ;
var disjoint_classes_check = false;
var domain_check = false;
var domain_validated = false;
var missing_datatype_check = false;
var term_type_check = false;
var count = 0;
var ontology_score = [];
var ontologies_assessed = false;



function validateTermType(resource) {
    try{
        // repair the term type if incorrect
        if(!term_type_check){
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if(!labelTriple) {
                    break;
                    return null;
                }
                var JavaCLass = Java.type("JavaClasses.TermType");
                var Result = JavaCLass.repairTermType(String(labelTriple.object));

            }
            term_type_check = true;

        }

    }

    catch (err) {
        print("ERROR REPAIRING TERM TYPE ", err )
    }


}


function getProperty($this, name) {
	var it = $data.find($this, TermFactory.namedNode(NS + name), null);
	var result = it.next().object;
	it.close();
	return result;
}


function assessOntologies(resource){
    return;
    if (!ontologies_assessed){
            	ontologies_assessed =true;
                print("ASSESSING ONTOLOGIES USED")
            	var labelProperty = TermFactory.namedNode(NS+"class");
            	var labels = $data.find(resource, labelProperty, null);
            	for(;;) {
            		var labelTriple = labels.next();
            		if(!labelTriple) {
                        break;
                        return null;
            		}
                    var JavaCLass = Java.type("JavaClasses.OntologyQualityAssessment");
                    var ResponseCode = JavaCLass.runTest(String(labelTriple.object));


                }
    }
}



function validateDuplicateTriples(resource) {
    try{

        columns = getAllValues("column");
        predicates = getAllValues("predicate");
        if (no_duplicates === false){
            return null;
        }
        comparison_list  =[];
        for (var i = 0; i < predicates.length; i++) {
            var tmp = [String(predicates[i]), String(columns[i])] ;
            comparison_list.push(tmp);
            tmp = [];

        }
        for (var i = 0; i < comparison_list.length; i++) {
            var occurrences = countOccurrences(comparison_list, comparison_list[i]);
            if (occurrences > 1){
                no_duplicates = false;
            }

        }
        return no_duplicates;
    }
    catch(err){
        print("validateDuplicateTriples ", err);
    }

}



function getAllValues(name, resource){
    try{
        var labelProperty = TermFactory.namedNode(NS+name);
        var labels = $data.find(resource, labelProperty, null);
        var result = [];
        for(;;) {
            var labelTriple = labels.next();
            if(!labelTriple) {
                break;
                return null;
            }

            var label = labelTriple.object;
            result.push(label);


        }
        return result;
    }
    catch(err){
        print("getAllValues ", err);
    }
}


function validateDisjointClasses(resource){
    try{
        if (!disjoint_classes_check){
            var classesURI = getAllValues("class");
            var JavaCLass = Java.type("JavaClasses.Disjoint");
            var result = JavaCLass.validateDisjointClasses(classesURI);
            disjoint_classes_check = true;
            return result;

        }
    }
    catch(err){
        print("validateDisjointClasses " + err);
    }
}

function validateDomain(resource) {
    try{
    	var results = [];
        if (!domain_validated){
        result = [] ;
      	var labelProperty = TermFactory.namedNode(NS+"predicate");
      	var labels = $data.find(resource, labelProperty, null);
      	for(;;) {
      		var labelTriple = labels.next();
      		if(!labelTriple) {
                  break;
                  return null;
      		}
      		var predicate = labelTriple.object;
            var JavaCLass = Java.type("JavaClasses.Domain");
            var result = JavaCLass.validateAllDomains(getAllValues("class", resource), String(predicate));
             if(result==true){
                  return true;
              }
            else{
                results.push({ value : predicate });
                }
            }
                domain_validated = true;

            return results;
            }
    }
    catch(err){
        print("validateDomain " + err);
     }
    }

function countOccurrences(numArray, num){
        try{
            var count = 0;
            for (var i = 0; i < numArray.length; i++  ){

                var bool = true;
                for (var j = 0; j < num.length; j++  ){
                    if (String(numArray[i][j]) != String(num[j])){
                        bool = false;
                        print(String(numArray[i][j]), String(num[j]), bool)
                    }
                    }

                  if (bool === true){
                        count++;
                  }
               }
            return count
        }
        catch(err){
            print("countOccurrences " + err);
            }
        }

function addMissingDataTypes(resource){
    try{
        var labelProperty = TermFactory.namedNode(NS+"predicate");
        var labels = $data.find(resource, labelProperty, null);
        if (!missing_datatype_check){
        for(;;) {
            var labelTriple = labels.next();
            if(!labelTriple) {
                break;
                return null;
            }
            var label = labelTriple.object;
            var JavaCLass = Java.type("JavaClasses.DataType");
            var result = JavaCLass.validateDataType(String(label), "./resources/sample_map.ttl");
        }
        missing_datatype_check = true;
        }
    }
    catch(err){
        print("addMissingDataTypes " + err);
        return true;
    }
}


function validateRange(resource){
    try{
        var predicate = getProperty(resource, "predicate");
    	var datatype = getProperty(resource, "datatype");
    	if (datatype && predicate){
    	    	print("CHECKING RANGE predicate " + predicate, "datatype " + datatype);
    	    	var JavaCLass = Java.type("JavaClasses.Range");
    		    var string_predicate = String(predicate);
    		    var string_datatype  = String(datatype);
    	        var result = JavaCLass.validateRange(predicate, datatype);
    	        return result;
    	}
    }

    catch(err){
        print("validateRange " + err);
    }
}
