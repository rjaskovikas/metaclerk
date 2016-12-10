#!/bin/bash

# JAVA_HOME="/c/Program Files/Java/jdk1.8.0_66"
PATH=${JAVA_HOME}/bin:$PATH
JAVA_OPTS=-Dfile.encoding=utf-8
MAIN_CLASS=org.rola.metaclerk.MetaClerk
LIBS=lib/*

java -cp "$LIBS" $JAVA_OPTS $MAIN_CLASS $*
