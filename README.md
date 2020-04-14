# R2RML Mapping Assessment Framework 
A framework which will assess and refine the quality of [R2RML](https://www.w3.org/TR/r2rml/)  mapping definitions.

This framework was designed using Java 11 and Maven. 
## Installation 
To install the dependencies required for running the code, execute the following [Maven](https://maven.apache.org/) commands. 

```bash
mvn clean
mvn compile
mvn dependency:copy-dependencies
```


## Using the code 
To assess and refine an R2RML mapping file, run the following command, where ```<mapping_file>``` , is replaced with the file name of the mapping, which you would like to input into the framework. 

```bash
java -cp "./target/classes:./target/dependency/*" ShaclTest "<mapping_file>"
```

An example mapping which can be found in ```/resources/sample_map.ttl``` can be run, using the following command.

```bash
java -cp "./target/classes:./target/dependency/*" ShaclTest "./resources/sample_map.ttl"
```

The validation report will be written to ```/resources/output.ttl```.

The refined mapping will be written to ```/resources/refined_mapping.ttl```.

## License

Code written by Alex Randles.

This study is supported the Science Foundation Ireland ADAPT Centre for Digital Content Technology (Grant 13/RC/2106).