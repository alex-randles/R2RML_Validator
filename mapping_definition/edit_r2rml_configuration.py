# connectionURL =
# mappingFile =/home/alex/Desktop/First_Experiment/r2rml/provenance/sample_map.ttl
# CSVFiles =/home/alex/Desktop/First_Experiment/r2rml/provenance/errors.csv
# outputFile =/home/alex/Desktop/First_Experiment/r2rml/provenance/output.ttl


# this function will create the "test.properties" file for R2RML
def create_test_properties(test_properties_file,  mapping_file, output_file, CSV_file="", connection_url=""):
    # It can either be passed a connection url argument or CSV file
    file_content = {"connectionURL": connection_url, "mappingFile": mapping_file, "CSVFiles": CSV_file , "outputFile":output_file}
    file_content = [k + "=" + v for k, v in file_content.items()]
    file_content = "\n".join(file_content)
    f = open(test_properties_file, "w")
    f.write(file_content)
    print("Test properties written to {}".format(test_properties_file))


if __name__ == "__main__":
    create_test_properties("test.properties", "/home/alex/Desktop/First_Experiment/r2rml/provenance/sample_map.ttl", "/home/alex/Desktop/First_Experiment/r2rml/provenance/output.ttl", "/home/alex/Desktop/First_Experiment/r2rml/provenance/errors.csv")