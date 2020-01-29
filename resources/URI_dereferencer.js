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
	        var ResponseCode = JavaCLass.getResponseCode(String(labelTriple.object));
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
	        var ResponseCode = JavaCLass.getResponseCode(String(labelTriple.object));
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
	        var ResponseCode = JavaCLass.getResponseCode(String(labelTriple.object));
	        datatype_URI_checked = true;
            return ResponseCode;
	    // return ResponseCode;
		}
		else{
		    return null;
		}
	}
}