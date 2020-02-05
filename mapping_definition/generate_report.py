import rdflib
import subprocess
import os
from pandas import DataFrame

# the file containing the SHACL validation report
report_file = "./resources/output.ttl"


def query_file(file_name, query):
    # A function that will query a turtle file
    g = rdflib.graph.Graph()
    g.parse(file_name, format='n3')
    query_result = g.query(query)
    return query_result


def get_invalid_shapes(file_name):
    # Query the SHACL output to get all errors, use optional as all errors dont include the same triples
    query = """ 
    PREFIX sh: <http://www.w3.org/ns/shacl#>
    SELECT ?subject ?focusNode ?resultMessage ?resultPath ?sourceConstraintComponent ?sourceShape ?value
	WHERE {
        OPTIONAL {	  ?subject 		   sh:focusNode ?focusNode. } 
        OPTIONAL {	  ?subject         sh:resultMessage ?resultMessage . } 
        OPTIONAL {	  ?subject         sh:resultPath ?resultPath . }
        OPTIONAL {	  ?subject         sh:resultSeverity ?severity . }
        OPTIONAL {	  ?subject         sh:sourceConstraintComponent ?sourceConstraintComponent . } 
        OPTIONAL {	  ?subject         sh:sourceShape ?sourceShape . }
        OPTIONAL {	  ?subject         sh:value ?value . }
        .
        }
	"""
    query_result = query_file(file_name, query)
    print("Result from querying turtle file - " + file_name )
    for result in query_result:
        print(result)
    remove_duplicates(query_result)
    return query_result


def remove_duplicates(query_results):
    # this didn't work as planned some parts of the result may be different....
    compare_list = []
    for result in query_results:
        compare_list.append([result[i] for i in range(0, 5)])
    print(22222)
    for element in compare_list:
        occurences = compare_list.count(element)
        print(occurences)
        if occurences >= 2:
            compare_list.remove(element)
    print(compare_list)


def check_conformity(file_name):
    # Checking whether the shape already conforms
    print(file_name)
    query = """ 
        PREFIX sh: <http://www.w3.org/ns/shacl#>
        SELECT ?subject ?result 
    	WHERE {
              ?subject sh:conforms ?result
            .
            }
    	"""
    query_result = query_file(file_name, query)
    conformity_result = "".join([str(triple[1]) for triple in query_result])
    print("conforimity check= " + conformity_result)
    return conformity_result


def parse_query_result(query_result):
    # Create the data frame for the report.csv file
    column_names = ["ID", "FOCUS_NODE", "RESULT_MESSAGE", "RESULT_PATH", "SOURCE_CONSTRAINT_COMPONENT", "SOURCE_SHAPE",
                    "VALUE", "MACHINE FIXABLE"]
    data = {}
    for name in column_names:
        data[name] = []
    row_index = 1
    num_selects = 7
    for result in query_result:
        data[column_names[0]].append(row_index)
        for i in range(1, num_selects):
            current_result = result[i]
            print("current result", current_result)
            # if no value add empty string
            if str(current_result) == "":
                data[column_names[i]].append(" ")
            else:
                data[column_names[i]].append(str(current_result))
            # Set to false as all errors cant be fixed by a machine yet...
        data[column_names[i + 1]].append("False")
        row_index += 1
    print(data)
    return data


def create_df(data, output_file):
    # output the result of SHACL test to "report.csv"
    df = DataFrame(data)
    df.to_csv(output_file, index=None, header=True)


def run_validation(data_file, shape_file, output_file):
    # run the SHACL test
    # subprocess.call("./compile.sh")
    print(data_file, shape_file, output_file)
    print(output_file)
    SHACL_command = 'java -cp "./target/classes:./target/dependency/*" ShaclTest "{}" "{}" "{}"'.format(data_file, shape_file, output_file)
    os.system(SHACL_command)
    # Parse output & output to CSV file
    print(report_file)
    query_result = get_invalid_shapes(output_file)
    data = parse_query_result(query_result)
    create_df(data, "./static/report.csv")
    print(report_file)
    return check_conformity(output_file)


if __name__ == "__main__":
    shapes = get_invalid_shapes("./resources/output.ttl")
    remove_duplicates(shapes)
    # run_validation("./resources/function.ttl", "./resources/sample_map.ttl")

