var NS = "http://www.w3.org/ns/r2rml#";
var duplicates_checked = false;
var disjoint_classes_check = false;

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

function isAccessible($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var JavaCLass = Java.type("JavaClasses.DereferenceURI");
            var result = JavaCLass.accessRDF(object);
            print("dererfence", result, object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
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
        labels.close();
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
            if (classesURI === undefined || classesURI.length === 0) {
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

function validateRange($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.Range");
            var result = JavaCLass.validateRange(object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
 }

function validateMachineLicense($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasMachineReadableLicense(object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
 }

function validateHumanLicense($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasHumanReadableLicense(object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
 }


function hasHumanReadableLabelling($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasHumamReadableLabelling(object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
 }

function validateBasicProvenance($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            print("provenance test ", object);
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasBasicProvenance(object);
            print("datatype", result, object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
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

// function validateUndefined($this) {
//     try{
//         var results = [];
//         var p = TermFactory.namedNode(NS+"predicate");
//         var s = $data.find($this, p, null);
//         for(var t = s.next(); t; t = s.next()) {
//             var object = t.object;
//             print("label", object, isUndefined(object));
//             var result = isUndefined(object);
//             if(result) {
//                 results.push({
//                     value : object
//                 });
//             }
//         }
//         s.close();
//         return results;
//     }
//     catch(err){
//         print("validateDomain " + err);
//         return true;
//     }
// }

function validateUndefined($this) {
    try{
        var result = ["hello", "bob"];
        var result2  = ["john"];
        var results = result.concat(result2);
        print(results, "testing");
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
}

// function validateUndefined($this) {
//     try{
//         var results = [];
//         var p = TermFactory.namedNode(NS+"predicate");
//         var s = $data.find($this, p, null);
//         for(var t = s.next(); t; t = s.next()) {
//             var object = t.object;
//             print("label", object, isUndefined(object));
//             var result = isUndefined(object);
//             if(result) {
//                 results.push({
//                     value : object
//                 });
//             }
//         }
//         s.close();
//         return results;
//     }
//     catch(err){
//         print("validateDomain " + err);
//         return true;
//     }
// }

function validateDatatype($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            print("test", object);
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.DataType");
            var result = JavaCLass.validateDatatype(object);
            print("datatype", result, object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
}

function validateDomain($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                print("undefined", undefined);
                break;
            }
            var JavaCLass = Java.type("JavaClasses.Domain");
            var result = JavaCLass.validateDomain(object);
            print("domain", result, object);
            if(!result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        print("validateDomain " + err);
        return true;
    }
}
