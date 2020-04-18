var NS = "http://www.w3.org/ns/r2rml#";

function getProperty($this, name) {
    try{
    	var it = $data.find($this, TermFactory.namedNode( name), null);
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
        print("Validating dereferencability of URIs....")
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
        print("Validating duplicate triple definitions....")
        var JavaCLass = Java.type("JavaClasses.DuplicateTriples");
        var result = JavaCLass.detectDuplicateTriples();
        return result;
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
        print("Validating usage of disjoint classes....")
        var classesURI = getAllValues("class");
        if (classesURI === undefined || classesURI.length === 0) {
            return true;
        }
        var JavaCLass = Java.type("JavaClasses.Disjoint");
        var result = JavaCLass.validateDisjointClasses(classesURI);
        return result;
    }
    catch(err){
        return true;
    }
}

function validateRange($this) {
    try {
        print("Validating usage of incorrect range....")
        var results = [];
        var p = TermFactory.namedNode(NS + "predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var predicate = t.object;
            var objectMap = getProperty($this, NS+"objectMap");
            var termType = getProperty(objectMap, NS+"termType");
            if (termType === undefined || String(termType) === NS + "BlankNode"){
                continue;
            }
            var JavaCLass = Java.type("JavaClasses.Range");
            var result = JavaCLass.validateRange(predicate, termType);
            if(!result) {
                results.push({
                    value: predicate
                });
            }
        }
        return results;
    }
    catch(err){
        return true;
    }
}

function validateDomainRangeDefinition($this) {
    try {
        print("Validating domain and range definition...")
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
            var result = JavaCLass.hasDomainRangeDefinition(object);
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
        print("Validating machine-readable license....")
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
        print("Validating human-readable license...")
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
        print("Validating human readable labels/comments....")
        var results = [];
        var p = TermFactory.namedNode(NS+"predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var predicate = t.object;
            var undefined = isUndefined(predicate);
            if (undefined){
                break;
            }
            var JavaCLass = Java.type("JavaClasses.VocabularyAssessment");
            var result = JavaCLass.hasHumamReadableLabelling(predicate);
            if(!result) {
                results.push({
                    value : predicate
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
        print("Validating basic provenance information....")
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
        print("Validating usage of undefined classes and properties....")
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

function validateDomain($this) {
    try{
        print("Validating usage of incorrect domain....")
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

function validateDatatype($this) {
    try {
        print("Validating usage of incorrect datatype....")
        var results = [];
        var p = TermFactory.namedNode(NS + "predicate");
        var s = $data.find($this, p, null);
        for(var t = s.next(); t; t = s.next()) {
            var predicate = t.object;
            var objectMap = getProperty($this, NS+"objectMap");
            var datatype = getProperty(objectMap, NS+"datatype");
            var JavaCLass = Java.type("JavaClasses.DataType");
            var result = JavaCLass.validateDatatype(predicate, datatype);
            if(!result) {
                results.push({
                    value : predicate
                });
            }
        }
        return results;
    }
    catch(err){
        return true;
    }
}

