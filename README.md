Complex Event Processing Platform
==============================

### Welcome to the Complex Event Processing Platform
This platform enables you to process complex events on the fly with the integrated event processing engine [Esper](http://esper.codehaus.org/). You can process and persist events from event logs of your company and enrich your BPMN models with monitoring points for easy analyzing of your workflows.

### Requirements
You should have installed Maven 3.0.3 or newer, Java 1.7, MySQL server version 14 or newer and a Tomcat server 7. Other setups had not be tested but may work. The project works for Windows 7, Debian and MacOSX 10.8. To run the platform website you need the free port 8081 or you could change it in the source code.


### Setup
* install signavio core components as described [here](https://code.google.com/p/signavio-core-components/)
* create 2 databases on your MySQL server _sushi_development_ and _sushi_testing_.
* copy the file SushiCommon/src/main/resource/META_INF/persistence_template.xml to SushiCommon/src/main/resource/META_INF/persistence.xml and edit the connection information of your sql server: 

```
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/[sushi_testing|sushi_development]" />
<property name="javax.persistence.jdbc.user" value="USERNAME" />
<property name="javax.persistence.jdbc.password" value="PASSWORD" />
```
* (if you dont need a webservice, leave this part out) Setup the configuration of your Tomcat server for the project "SushiWebservice"  as described here: http://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/
* install the maven projects locally on your computer from the root folder with:

```
mvn -f jbpt/pom.xml clean install
mvn -f jbpt/jbpt-core/pom.xml clean install
mvn -f jbpt/jbpt-deco/pom.xml clean install
mvn -f Esper/pom.xml clean install
mvn -f SushiCommon/pom.xml clean install
mvn -f SushiImport/pom.xml clean install
mvn -f SushiEsper/pom.xml clean install
mvn -f SushiSimulation/pom.xml clean install
mvn -f SushiWicket/pom.xml clean install
mvn -f SushiWebservice/pom.xml clean install
```

### Running
* To run the platform `mvn -f SushiWicket/pom.xml jetty:run`. The WebUI should now be accessible under [http://localhost:8081/SushiWicket](http://localhost:8081/SushiWicket).
* To run the Webservice:

```
export MAVEN_OPTS=-Xmx512m
mvn -f /home/platformaccount/sushi/SushiWebservice/pom.xml clean tomcat:undeploy
mvn -f /home/platformaccount/sushi/SushiWebservice/pom.xml clean install tomcat:deploy
```

### Usage
You can upload on the WebUI different kinds of data. The platform supports XLS, CSV, XML(+XSD) and EDIFACT files. If you want to use your event logs you need to create an event type first. 
For XLS and CSV you can do this on the fly after uploading a XLS oder CSV or beforehand in the event repository tab. XML files need an Eventtype described by an XSD file. The XML Events have to refer to this XSD file in the header _xsi:noNamespaceSchemaLocation="URI_TO_XSD.xsd"_.
For further explanation feel free do read the user manual.

### Licence
Licence GNU General Public License (GPL) (GPL v2) http://opensource.org/licenses/gpl-2.0.php
