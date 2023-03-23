#!/bin/bash
jar cvf fw.jar etu1947
mkdir ../../../../../TestFramework/lib
cp fw.jar ../../../../../TestFramework/lib/
cd ~
pwd
cd Documents/'Semestre 4'/'Mr Naina'/
pwd
jar -cvf TestFramework.war TestFramework
pwd
cp TestFramework.war ../../Install/apache-tomcat-10.0.27/webapps   