var NS = "http://www.w3.org/ns/r2rml#";
var duplicates_checked = false;
var disjoint_classes_check = false;
var machine_license_validated = false;
var human_license_validated = false;
var domain_check = false;
var domain_validated = false;
var missing_datatype_check = false;
var term_type_check = false;
var datatype_validated = false;
var labelling_validated = false;
var accessibility_validated = false;
var basic_provenance_validated = false;
var vcabulary_completeness_validated = false;
var range_validated = false;


function getProperty($this, name) {
    try{
    	var it = $data.find($this, TermFactory.namedNode(NS + name), null);
    	var result = it.next().object;
    	it.close();
    	return result;
    }
    catch(err){
         return;
    }
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
                if (!isUndefined(predicate)){
                    break;
                    return null;
                }
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
        return true;
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
        return true;
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
        return true;
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
        return;
    }
}


function validateDisjointClasses(resource){
    try{
        if (!disjoint_classes_check){
            var classesURI = getAllValues("class");
            if (classesURI === undefined || classesURI.length == 0) {
                   return true;
               }
            var JavaCLass = Java.type("JavaClasses.Disjoint");
            var result = JavaCLass.validateDisjointClasses(classesURI);
            disjoint_classes_check = true;
            return result;
        }
    }
    catch(err){
        print("validateDisjointClasses " + err);
        return true;
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
                 if (isUndefined(predicate)){
                           break;
                           return null;
                 }
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
        return true;
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
        return true;
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
                 if (!isUndefined(predicate)){
                           break;
                           return null;
                 }
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
        print("machine license error  " + err);
        return true;
    }
}

function validateHumanLicense(resource) {
    try{
        var results = [];
        if (!human_license_validated){
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
                 if (!isUndefined(predicate)){
                           break;
                           return null;
                 }
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.hasHumanReadableLicense(String(predicate));
                if (!result) {
                    results.push({value: predicate});
                }
            }
            human_license_validated = true;
            return results;
        }
    }
    catch(err){
        print("human license " + err);
        return true;
    }
}

function validateBasicProvenance(resource) {
    try{
        var results = [];
        if (!basic_provenance_validated){
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
                 if (!isUndefined(predicate)){
                           break;
                           return null;
                 }
                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
                var result = JavaCLass.hasBasicProvenance(String(predicate));
                if (!result) {
                    results.push({value: predicate});
                }
            }
            basic_provenance_validated = true;
            return results;
        }
    }
    catch(err){
        print("human license " + err);
        return true;
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
            if (isUndefined(predicate)){
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
        return true;
     }
    }

function isUndefined(URI){
           var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
           var result = JavaCLass.validateUndefined(String(URI));
           return !result;
}

//
//function validateUndefined(resource) {
//    try{
//        var results = [];
//        if (!vcabulary_completeness_validated){
//            result = [] ;
//            var labelProperty = TermFactory.namedNode(NS+"predicate");
//            var labels = $data.find(resource, labelProperty, null);
//            for(;;) {
//                var labelTriple = labels.next();
//                if (!labelTriple) {
//                    break;
//                    return null;
//                }
//                var predicate = labelTriple.object;
//                var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
//                var result = JavaCLass.validateUndefined(predicate);
//                if (!result) {
//                    results.push({value: predicate});
//
//                }
//            }
//            vcabulary_completeness_validated = true;
//            return results;
//        }
//    }
//    catch(err){
//        print("validateDomain " + err);
//        return true;
//    }
//}


function validateUndefined($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            print("label", object, isUndefined(object));
            var result = isUndefined(object);
            if(result) {
                results.push({
                    value : object
                });
            }
        }
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
}



function getEnglishLabel(resource) {
	var labelProperty = TermFactory.namedNode(NS+"predicate");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		if(!labelTriple) {
			return null;
		}
		var label = labelTriple.object;
		print("label ", label);
	}
}
