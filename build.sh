#!/bin/sh
var=$(pwd)
tomcat=$var/Documents/Install/apache-tomcat-10.0.27/
frameworkPlace=$var/Documents/Semestre_4/Mr_Naina/Framework-main/Framework/
tomcatBin=/home/layah/Documents/Install/apache-tomcat-10.0.27/bin/

#WhereIs your framework
yourFramework=$var/Documents/Semestre_4/Mr_Naina/Framework-main/TestFramework
yourFrameworkName=TestFramework/

cd -- "$frameworkPlace"
echo ${frameworkPlace}
find -name "*.java" > sources.txt
javac -d "classes" @sources.txt

if [ -f "$yourFramework/WEB-INF/lib" ] && [ -f "$yourFramework/WEB-INF/classes"]
then
    echo "Directory exists"
else
    cd -- "$yourFramework"
    mkdir -p WEB-INF
    mkdir -p WEB-INF/lib
    mkdir -p WEB-INF/classes
    cd -- "$frameworkPlace"
fi

cd -- "$frameworkPlace/lib"
cp postgres.jar "$yourFramework/WEB-INF/lib"
cp gson.jar "$yourFramework/WEB-INF/lib"


cd -- "$frameworkPlace/classes"
jar -cf build.jar . && cp build.jar "$yourFramework/WEB-INF/lib"
cd -- "$yourFramework"
find -name "*.java" > yoursources.txt
javac -cp ./WEB-INF/lib/build.jar -d "./WEB-INF/classes/" @yoursources.txt
cp -r "$yourFramework/WEB-INF" "$yourFramework/web"
pwd

cd -- "$yourFramework"
cp "$yourFramework/web/WEB-INF/web.xml" "$yourFramework/WEB-INF"

cd -- "$yourFramework"

mkdir -p Framework
cp -r WEB-INF Framework
cp Pages/* Framework
cp -r Pages/assets Framework/assets

cd Framework

jar -cf YourFramework.war . && cp YourFramework.war "$tomcat/webapps"

cd -- "$tomcatBin"
./catalina.sh stop
./catalina.sh start
