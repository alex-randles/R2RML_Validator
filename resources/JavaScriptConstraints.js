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
        return true;
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
        return true;
    }
 }

function validateMachineLicense($this) {
    try{
        var results = [];
        var namespaces = getUniqueNamespaces($this);
        for (i = 0; i < namespaces.length; i++) {
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasMachineReadableLicense(namespaces[i]);
            if (!result) {
                results.push({
                    value: TermFactory.namedNode(namespaces[i])
                });
            }
        }
        return results;
        }
        catch(err){
            return true;
        }
    }

function validateHumanLicense($this) {
    try{
        var results = [];
        var namespaces = getUniqueNamespaces($this);
        for (i = 0; i < namespaces.length; i++) {
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasHumanReadableLicense(namespaces[i]);
            if (!result) {
                results.push({
                    value: TermFactory.namedNode(namespaces[i])
                });
            }
        }
        return results;
    }
    catch(err){
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
        return true;
    }
 }

function validateBasicProvenance($this) {
    try{
        var results = [];
        var namespaces = getUniqueNamespaces($this);
        for (i = 0; i < namespaces.length; i++) {
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasBasicProvenance(namespaces[i]);
            if (!result) {
                    results.push({
                        value: TermFactory.namedNode(namespaces[i])
                    });
            }
        }
        return results;
    }
    catch(err){
        return true;
    }
}

function getUniqueNamespaces($this) {
    try{
        var namespaces = [];
        var p = TermFactory.namedNode(NS+"class");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var newObject  = object.toString();
            var namespace = getNamespace(newObject);
            if ( namespaces.indexOf(namespace) === -1){
                 namespaces.push(namespace);
            }
        }
        s.close();
        return namespaces;
    }
    catch(err){
        return true;
    }
}

function getNamespace(URI){
    if (URI.indexOf('#') > -1){
        var prefix = URI.split("#").slice(0, -1);
        return prefix.join("#") + "#";
    }
    else if(URI.indexOf('/') > -1){
        var prefix = URI.split("/").slice(0, -1);
        return prefix.join("/") + "/";
    }
    return URI;
}

function isUndefined(URI){
       var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
       var result = JavaCLass.validateUndefined(String(URI));
       return !result;
}

function validateUndefined($this) {
    try{
        var undefinedProperties = validateUndefinedProperties($this);
        var undefinedClasses = validateUndefinedClasses($this);
        var results = undefinedProperties.concat(undefinedClasses);
        return results;
    }
    catch(err){
        return true;
    }
}

function validateUndefinedProperties($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var result = isUndefined(object);
            if(result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        return true;
    }
}

function validateUndefinedClasses($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"class");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var result = isUndefined(object);
            if(result) {
                results.push({
                    value : object
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
        return true;
    }
}

function validateDatatype($this) {
    try{
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var object = t.object;
            var undefined = isUndefined(object);
            if (undefined){
                break;
            }
            var JavaCLass = Java.type("JavaClasses.DataType");
            var result = JavaCLass.validateDatatype(object);
            if(!result) {
                results.push({
                    value : objclass_nameect
                });
            }
        }
        s.close();
        return results;
    }
    catch(err){
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
                break;
            }
            var JavaCLass = Java.type("JavaClasses.Domain");
            var result = JavaCLass.validateDomain(object);
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
        return true;
    }
}
