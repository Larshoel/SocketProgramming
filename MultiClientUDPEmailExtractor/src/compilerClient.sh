#!/bin/bash

file="UDPEmailExtractorClient"
if [  -f "$file.class" ]
then
    rm  $file.class
fi
javac $file.java
java $file
