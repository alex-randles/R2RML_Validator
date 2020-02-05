#!/bin/bash 
mvn clean
mvn compile
mvn dependency:copy-dependencies

