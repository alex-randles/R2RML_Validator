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
var missing_datatype_check = false;
var term_type_check = false;
var count = 0;
var ontology_score = [];


function calculateOntologyScore(resource) {
    try{



	var labelProperty = TermFactory.namedNode(NS+"predicate");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		if(!labelTriple) {
            break;
            return null;
		}
                        var JavaCLass = Java.type("JavaClasses.TermType");
                        var ResponseCode = JavaCLass.repairTermType(String(labelTriple.object));


    }
    }






// this carry out checks from mapping consistency in Ademars paper

function checkTermType(resource) {
    try{


	var labelProperty = TermFactory.namedNode(NS+"predicate");
	var labels = $data.find(resource, labelProperty, null);
	for(;;) {
		var labelTriple = labels.next();
		if(!labelTriple) {
            break;
            return null;
		}
                        var JavaCLass = Java.type("JavaClasses.TermType");
                        var ResponseCode = JavaCLass.repairTermType(String(labelTriple.object));


    }
    }


    catch (err) {
        print("error dereferencing URI ", err )
    }


}

function checkColumnLength($this) {
    try{
    	var current_object  = getProperty($this, "column");
    	if (current_object != null){
    	    // var current_object = "http://www.w3.org/ns/r2rml#Literal";
    	    if (current_object.lex.length == 0){
    	        return false;
    	    }

    	}




    }
    catch (err) {
        print("error dereferencing URI ", err )
    }

}

function checkDataType($this) {
    try{
    	var current_object  = getProperty($this, "datatype");
    	if (current_object != null){
    	    var JavaCLass = Java.type("test.TestURI");
	        var ResponseCode = JavaCLass.checkRDF(current_object);
	        return ResponseCode;
    	}

    	if (count == 1){
    	    return true;
    	}


    }
    catch (err) {
        print("error dereferencing URI ", err )
    }

    // print("current count", count);
}



function checkDuplicateColumns($this) {
    try{
    	var current_object  = getProperty($this, "tableName");
    	if (current_object != null){
    	    print("tableName is ", current_object);
    	    var JavaCLass = Java.type("test.TestURI");
	        var ResponseCode = JavaCLass.getResponseCode(current_object);
	        return true;
	        // return ResponseCode;
    	}

    	if (count == 1){
    	    return true;
    	}


    }
    catch (err) {
        print("error dereferencing URI ", err )
    }
}

function getProperty($this, name) {
	var it = $data.find($this, TermFactory.namedNode(NS + name), null);
	var result = it.next().object;
	it.close();
	return result;
}


function testing(resource) {
    if (!duplicates_checked){
        var tmp = getSubjects("subjectMap", resource);
        duplicates_checked = true;
    }
    return;

    return;
    resource, columns = getAllValues("column", resource);
    print("subjectMap", subjectMap);
    print("columns", predicates);
    return;
    predicates = getAllValues("predicate", resource);
    print("subjectMap", subjectMap);
    print("PREDICATES", predicates);
    print("COLUMNS", columns);
    if (no_duplicates === false){
        return null;
    }

    print("PREDICATES", predicates);
    print("COLUMNS", columns);
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


function calculateRangeDefinitionCoverage(resource){
    predicates = getAllValues("predicate", resource);
    //for(String predicate: predicates){
    // }

}

function getSubjects(name, resource){
	var columnProperty = TermFactory.namedNode(NS+"column");
	var columnLabel = $data.find(resource, columnProperty, null);
	var subjectLabelProperty = TermFactory.namedNode(NS+"subjectMap");
	var subjectLabel = $data.find(resource, subjectLabelProperty, null);
	var predicateLabelProperty = TermFactory.namedNode(NS+"predicate");
	var predicateLabel = $data.find(resource, predicateLabelProperty, null);
	var current_subject = null;
	var result = [];
	print("RESOURCE", resource);
	for(;;) {
		var columnTriple = columnLabel.next();
		var subjectTriple = subjectLabel.next();
		var predicateTriple = predicateLabel.next();
	    // var columnTriple = getProperty(resource, "column");
	// 	var subjectTriple =  getProperty(resource, "subjectMap");
		// var predicateTriple =  getProperty(resource, "predicate");
		print("COLUMN TRIPLE", columnTriple);
		print("PREDICATE TRIPLE", predicateTriple);
		print("SUBJECT TRIPLE", subjectTriple);

	    if(!subjectTriple) {
	        break;
		    // print(subjectTriple.object);
		}
		if (!columnTriple  ){
		    print("BREAKING");
		    break;
		}

		if(predicateTriple) {
		    result.push(predicateTriple.object);
		    // print(predicateTriple.object);
		}
		if(columnTriple){
				    result.push(columnTriple.object);
                //    print(columnTriple.object);
		}

        // print(result);
	   if(subjectTriple) {
	   		    mappings.current_subject = result;

		    var current_subject = String(subjectTriple.object);
		    // print("NEW SUBJECT", result);

		    result =[];
		}



	}


        print("RETUNRING mappings");
var count = 0;
for (var i in mappings) {
   if (mappings.hasOwnProperty(i)) count++;
}
for (var prop in mappings) {
  print("obj." + prop + " = " + mappings[prop]);
}
print("LENGTH", count);

	    return mappings;
}

function checkDuplicateTriples(resource) {
    columns = getAllValues("column");
    predicates = getAllValues("predicate");
    print("PREDICATES", predicates);
    print("COLUMNS", columns);
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

function checkDuplicateTriples(resource) {
    columns = getAllValues("column");
    predicates = getAllValues("predicate");
    print("PREDICATES", predicates);
    print("COLUMNS", columns);
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




function test(resource) {
    columns = getAllValues("column");
    predicates = getAllValues("predicate");
    print("PREDICATES", predicates);
    print("COLUMNS", columns);
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

function validateGermanLabel($this) {
    if (duplicates_checked){
        return;
    }
	var results = [];
    print($this);
    var i = 0;
	for(;;) {
	    var p = TermFactory.namedNode(NS + "column");
		var s1 = $data.find($this,p,null);
		print(s1.next());
	    var p1 = TermFactory.namedNode(NS + "predicate");
		var s2 = $data.find($this,p1,null);
		print(s2.next());
	    var p2 = TermFactory.namedNode(NS + "subjectMap");
		var s3 = $data.find($this,p2,null);
		print(s3.next());
		i++;
		if (i===20){
		    break;
		}
	}
	duplicates_checked = true;
	return results;
}

function getAllValues(name, resource){
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

function compare_template_constant(resource){
    if (!constant_template_checked){
            constant_template_checked = true;
            template_count = getAllValues("template").length;
            constant_count = getAllValues("constant").length;
            print(template_count, constant_count);
            if (constant_count < template_count){
                return false;
            }
            return true;
    }



}

function getTableNameCount(resource){
    var arr = getAllValues("tableName");
    return true;
    var unique_names = [];
    for (var i = 0; i < arr.length; i++){
        if(countOccurrences(arr, arr[i]) > 1){
          arr.splice(i, 1);
        }
        else{
            unique_names.push(arr[i]);
        }
    }
    print("*****", unique_names);
    var table_count = unique_names.length;
    if (!tables_count_check){
        tables_count_check = true;
        if (table_count > 1){
         return false;
         }
         return true;
    }
}

function countOccurrences(numArray, num){
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



function checkDuplicateElements(numArray){
            var current_count = 0;
            for (var i = 0; i < numArray.length; i++  ){
                for (var j = 0; j < numArray.length; j++  ){
                    if (numArray[i]===numArray[j]){
                        current_count++;
                    }
                }
                if (current_count>1){
                    return true;
                }
                current_count = 0;
               }
            return false;
        }




function validateDisjointClasses(resource){
    if (!disjoint_classes_check){
        var classesURI = getAllValues("class");
        print("CLASSSESSSSSSSSSSS" , classesURI);
        var JavaCLass = Java.type("JavaClasses.DereferenceURI");
        var result = JavaCLass.validateDisjointClasses(classesURI);
        disjoint_classes_check = true;
        return result;

    }

}


function checkDomain($this) {
	var results = [];
	// var classURI = getProperty($this, "class");
	print("Testing domain");
//	var p = TermFactory.namedNode(NS+"predicate");
//	var s = $data.find($this, p, null);
//	var
//	for(var t = s.next(); t; t = s.next()) {
//		var object = t.object;
//			print(object);
//		}
    var predicate  = getProperty($this, "predicate");
   //  var classes  = getProperty($this, "class");
 if (predicate){

      print("PREDICATE", predicate);
    print("CLASS", getAllValues("class"));
    var JavaCLass = Java.type("JavaClasses.DereferenceURI");
    var result = JavaCLass.validateAllDomains(getAllValues("class"), String(predicate));
    print("RESULT OF TESTING DOMAIN", result);
    if(result==true){
        return true;
    }
    return String(predicate);
    return result;


  }

}


function addMissingDataTypes(resource){
	var labelProperty = TermFactory.namedNode(NS+"predicate");
	var labels = $data.find(resource, labelProperty, null);
	if (!missing_datatype_check){
	for(;;) {
		var labelTriple = labels.next();
		if(!labelTriple) {
            break;
            return null;
		}
		print("DATATYPE CHECKING");
				var label = labelTriple.object;

				     var JavaCLass = Java.type("JavaClasses.DereferenceURI");
    var result = JavaCLass.fixDataType(String(label), "./resources/sample_map.ttl");

count++;
    print("ADDING DATAYTPE !!!!!!");



	}
	domain_check = true;
	return;




	}
}






function square($number) {
    print("fufdyfhhfhfh");
	return $number;
}




function validateRange(resource){
    var predicate = getProperty(resource, "predicate");
	var datatype = getProperty(resource, "datatype");
	if (datatype && predicate){
	    	print("CHECKING RANGE predicate " + predicate, "datatype " + datatype);
	    	var JavaCLass = Java.type("JavaClasses.DereferenceURI");
		    print(String(predicate), "373673")
		    var string_predicate = String(predicate);
		    var string_datatype  = String(datatype);
	        var result = JavaCLass.validateRange(predicate, datatype);
	        return result;
	}


}
