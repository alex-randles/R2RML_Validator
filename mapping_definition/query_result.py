import rdflib
import time
g = rdflib.graph.Graph()
# the file containing the shacl validation report 
file_name = "/home/alex/Desktop/R2RML_Validator/mapping_definition/mapping_output/resources/output.ttl"


def query_file(file_name, query):
    g.parse(file_name, format='n3')
    query_result = g.query(query)
    return query_result


def create_error_dic(query_result):
    # maps the ID of the focus node to the value of the result path for use when manipulating the mapping
    # e.g (rdflib.term.Literal('2'), rdflib.term.Literal('WEBSITE'))
    error_dic = {}
    for result in query_result:
        error_dic[int(result[0])] = str(result[1])
    return error_dic


def get_invalid_shapes(SHACL_output):
    # put the invalid shapes in an easier to use format
    query = """ 
    PREFIX SH: <http://www.w3.org/ns/shacl#> 
    SELECT ?id ?item
	WHERE {
	  ?subject SH:focusNode ?focusNode; 
			   SH:resultPath ?resultPath
	  BIND(SUBSTR(str(?focusNode), STRLEN(str(?focusNode))) AS ?id)
	  BIND(UCASE(STRAFTER(str(?resultPath), "voc/")) AS ?item)
	} """
    query_result = query_file(SHACL_output, query)
    error_dic = create_error_dic(query_result)
    return error_dic


def check_conformity(SHACL_output):
    print("*************************\*n*************")
    print("CHECKING CONFORMITY")
    f = open(SHACL_output)
    print(f.read())
    query = """
    PREFIX SH: <http://www.w3.org/ns/shacl#> 
    SELECT ?conformity
    	WHERE {
    	  ?subject SH:conforms ?conformity .
    			   
    	} """
    query_result = query_file(SHACL_output, query)
    conformity = bool([result[0] for result in query_result][0])
    print("****************\n *****")
    print("conformity is " + str(conformity))
    # time.sleep(10)
    return conformity

if __name__ == "__main__":
    check_conformity("/home/alex/Desktop/R2RML_Validator/mapping_definition/resources/output.ttl")
    get_invalid_shapes(file_name)
