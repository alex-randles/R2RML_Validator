var NS = "http://www.w3.org/ns/r2rml#";
var duplicates_checked = false;
var disjoint_classes_check = false;
var low_latency_validated = false;
var machine_license_validated = false;
var domain_check = false;
var domain_validated = false;
var missing_datatype_check = false;
var term_type_check = false;
var datatype_validated = false;
var labelling_validated = false;
var accessibility_validated = false;
var vcabulary_completeness_validated = false;
var range_validated = false;

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



function hasHumanReadableLabelling(resource) {
    try{
        var results = [];
        if (!labelling_validated){
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
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.hasHumamReadableLabelling(predicate);
                if(!result){
                    results.push({ value : predicate });
                }
            }
            labelling_validated = true;
            return results;
        }
    }
    catch(err){
        print("validateDomain " + err);
    }
}


function isAccessible(resource) {
    try{
        var results = [];
        if (!accessibility_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.isAccessible(String(predicate));
                if (!result) {
                    results.push({value: predicate});

                }
            }
            accessibility_validated = true;

            return results;
        }

    }
    catch(err){
        print("accessibility error  " + err);
    }
}



function validateDuplicateTriples(resource) {
    try{
        if(!duplicates_checked){
            var JavaCLass = Java.type("JavaClasses.DuplicateTriples");
            var result = JavaCLass.detectDuplicateTriples();
            duplicates_checked = true;
            return result;
        }
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


function validateDatatype(resource) {
    try{
        var results = [];
        if (!datatype_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.DataType");
                var result = JavaCLass.validateDatatype(String(predicate));
                if (!result) {
                    results.push({value: predicate});
                }
            }
            datatype_validated = true;

            return results;
        }
    }
    catch(err){
        print("validateDomain " + err);
    }
}

function validateRange(resource) {
    try{
        var results = [];
        if (!range_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.Range");
                var result = JavaCLass.validateRange(String(predicate));
                if (!result) {
                    results.push({value: predicate});
                }
            }
            range_validated = true;
            return results;
        }
    }
    catch(err){
        print("validateDomain " + err);
    }
}

function validateLowLatency(resource) {
    try{
        var results = [];
        if (!low_latency_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.lowLatency(String(predicate));
                if (!result) {
                    results.push({value: predicate});

                }
            }
            low_latency_validated = true;

            return results;
        }
    }
    catch(err){
        print("validateDomain " + err);
    }
}


function validateMachineLicense(resource) {
    try{
        var results = [];
        if (!machine_license_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.hasMachineReadableLicense(String(predicate));
                if (!result) {
                    results.push({value: predicate});

                }
            }
            machine_license_validated = true;

            return results;
        }
    }
    catch(err){
        print("validateDomain " + err);
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
            if (!labelTriple) {
                break;
                return null;
            }
            var predicate = labelTriple.object;
            var JavaCLass = Java.type("JavaClasses.Domain");
            var result = JavaCLass.validateAllDomains(getAllValues("class", resource), String(predicate));
            if (!result) {
                results.push({value: predicate});

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




function ValidateVocabularyCompleteness(resource) {
    try{
        var results = [];
        if (!vcabulary_completeness_validated){
            result = [] ;
            var labelProperty = TermFactory.namedNode(NS+"predicate");
            var labels = $data.find(resource, labelProperty, null);
            for(;;) {
                var labelTriple = labels.next();
                if (!labelTriple) {
                    break;
                    return null;
                }
                var predicate = labelTriple.object;
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.vocabularyCompleteness(predicate);
                if (!result) {
                    results.push({value: predicate});

                }
            }
            vcabulary_completeness_validated = true;

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




