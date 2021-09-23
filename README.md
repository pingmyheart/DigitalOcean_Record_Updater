<h1>DigitalOcean Record Updater</h1>
<h2>Reference Documentation</h2>

<h3>How to Use</h3>
DigitalOcean Record Updater need two fundamental properties:
<ul>
    <li>DigitalOcean project name</li>
    <li>DigitalOcean bearer token</li>
</ul>
Both the properties have to be settled into the <b>application.yml</b> file to let spring project work as it should do.
<br>Once both the properties are settled, compile the project with following maven command
<br><br>
    <code>mvn -U clean package install -DskipTests</code>
<br><br>
Once the project is compiled, maven will automatically generate <b>target</b> folder with <b>.jar file</b>.<br>
DigitalOcean Record Updater works with java args passed to main class that represents the domain records to be updated<br>
Records to be updated must be passed to jar file through command line args like below
<br><br>
    <code style="text-align: center">java -jar Snapshot.jar pippo pluto paperino</code>
<br><br>
Obviously, if containerized, the domains have to be passed into <b>Dockerfile</b> like below
<br><br>
    <code style="text-align: center">ENTRYPOINT ["java", "-jar", "app.jar", "pippo", "pluto", "paperino"]</code>
<br>
<h3>Important</h3>
If the project name is <b>example.com</b> and the record to be updated is <b>pippo.example.com</b>, the argument to be passed to 
jar file il <b>pippo</b>.<br>
DigitalOcean Record Updater works only with record type A that have two fundamental parameters:
<ul>
    <li>Name -> that represents the domain name to be updated</li>
    <li>Data -> that represents the ip address pointed by the domain</li>
</ul>
In both container and terminal directly, DigitalOcean Record Updater works fine because it get the public ip address directly from 
<a href="ipinfo.io/ip">IpInfo.io</a>
<h3>Why Spring Boot</h3>
In origin, there was a python script that worked fine but since I am crazy, I wanted to convert it into a Spring Boot project.
<br><b>Easy</b>