var NS = "http://www.w3.org/ns/r2rml#";
var datatype_URI_checked = false;
var class_URI_checked = false;
var predicate_URI_checked = false;


function dereferenceClassURI(resource) {
	var labelProperty = TermFactory.namedNode(NS+"class");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		print(labelTriple);
		if(!labelTriple) {
			return null;
		}
		if (!class_URI_checked){
		    var JavaCLass = Java.type("JavaClasses.DereferenceURI");
	        var ResponseCode = JavaCLass.checkRDF(String(labelTriple.object));
	        class_URI_checked = true;
            return ResponseCode;
		}
		else{
		    return null;
		}
	}
}

function dereferencePredicateURI(resource) {
	var labelProperty = TermFactory.namedNode(NS+"predicate");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		print(labelTriple);
		if(!labelTriple) {
			return null;
		}
		if (!predicate_URI_checked){
		    var JavaCLass = Java.type("JavaClasses.DereferenceURI");
	        var ResponseCode = JavaCLass.checkRDF(String(labelTriple.object));
	        predicate_URI_checked = true;
            return ResponseCode;
		}
		else{
		    return null;
		}
	}
}


function dereferenceDataTypeURI(resource) {
	var labelProperty = TermFactory.namedNode(NS+"datatype");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		print(labelTriple);
		if(!labelTriple) {
			return null;
		}
		if (!datatype_URI_checked){
		    var JavaCLass = Java.type("JavaClasses.DereferenceURI");
	        var ResponseCode = JavaCLass.checkRDF(String(labelTriple.object));
	        datatype_URI_checked = true;
            return ResponseCode;
		}
		else{
		    return null;
		}
	}
	}



function dereferenceURI($this) {
    try{
    	var current_object  = getProperty($this, "class");
    	// if no object exist return true
    	if (current_object == null) {
    	    // print("no object exist")
    	    return true;
    	}

    	print("Tfgjfjh ", current_object)
    	// use method from java class to check URI response code
    	var JavaCLass = Java.type("JavaClasses.DereferenceURI");
        var ResponseCode = JavaCLass.accessRDF(String(current_object));
        // print("response code", ResponseCode);
        print(ResponseCode);
        if (response == false){
             return String(current_object);
        }
        return ResponseCode;
        return true;
	    // return ResponseCode;
    }
    catch (err) {
        print(err)
        // print("error dereferencing URI ", err )
    }

}

function testingFunction($this) {
    try{
        var next_object = getProperty($this, "column");
        var current_object  = getProperty($this, "predicate");
    	 print("predicate is " , current_object)
    	 print("column is ", next_object)
    	//}

        return true;
	    // return ResponseCode;
    }
    catch (err) {
        print("testing function error" + err)
    }

}


function getProperty($this, name) {
    // try - catch if object doesnt exist for this triple
    try {
        var it = $data.find($this, TermFactory.namedNode(NS + name), null);
        var result = it.next().object;
        // it.close();
        // print("result ", result);
        return result;
	}
    catch(err) {
        // print
        }
        }