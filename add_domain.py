import rdflib
import time
from rdflib import URIRef, BNode, Literal

g = rdflib.graph.Graph()
# the file containing the shacl validation report
file_name = "/home/alex/Desktop/First_Experiment/mapping_definition/mapping_output/resources/output.ttl"


def query_file(file_name):
    g.parse(file_name, format='n3')
    s = URIRef("http://www.w3.org/ns/r2rml#subjectMap")
    p = URIRef("http://www.w3.org/ns/r2rml#class")
    o = URIRef("http://dbpedia.org/ontology/Person")
    t = g.add((s,p,o))
    print(t)
    return
    query_result = g.query(query)
    return query_result




def add_domain(SHACL_output):
    # put the invalid shapes in an easier to use format
    query = """ 
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
INSERT DATA {
   <http://www.w3.org/ns/r2rml#subjectMap> <http://www.w3.org/ns/r2rml#class> <http://dbpedia.org/ontology/Person>. 
  
} """
    query_result = query_file(SHACL_output)
    return query_result


if __name__ == "__main__":
    query_file("./resources/sample_map.ttl")
    add_domain("./resources/sample_map.ttl")
