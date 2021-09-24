# DigitalOcean Record Updater

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://coveralls.io/github/codecentric/springboot-sample-app?branch=master)

## Requirements

For Building and Running the application you need:

* [AmazonCorretto11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)
* [Maven 3+](https://archive.apache.org/dist/maven/maven-3/)
* [Docker](https://docs.docker.com/get-docker/) (Only if you want to deploy it into a container)
* [Docker Compose](https://docs.docker.com/compose/install/) (Only if you want to deploy it into a container)

## About

DigitalOcean Record Updater is a spring boot application that permit to anyone who has a DigitalOcean account with DNS
project and want to manage them dynamically depending on a specific location of a device/server where the application is
going to be deployed.  
Basically, DigitalOcean Record Updater works
with [DigitalOcean API](https://docs.digitalocean.com/reference/api/api-reference/)
to update **type A DNS records** retrieving the current public ip address and updating it to a set of domains passed to
the application.

## How it works

Taking into account that you can have a single project or multiple project managed into DigitalOcean, the application
can work in both the scenarios in fact, depending on a property configured into the **application.yml**, you can decide
to run the application in multi-project mode or in single project mode. The property, settled false by default, is the
following

```properties
config:
    project:
        use-multi-project:false
```

### Run in single-project mode

To run the application in single project mode, it is necessary to check if *use-multi-project* property is settled to
false or if it is completely absent(by default it is settled to *false*).  
It is necessary to set the project name property and DigitalOcean read/write permission Bearer Token as following

```properties
config:
    authentication:
        bearer-token:digital_ocean_token
    project:
        name:example.com
```

If those fields are not settled, the application will interrupt the startup.  
Working on command line arguments, the domains to be updated have to be passed to compiled jar file that can be found
into
*target* folder by running the following command

```shell
mvn -U clean package install -DskipTests
```

The command will produce *DO_Record_Updater-0.0.1-SNAPSHOT.jar* file that will be used to run the application.  
Taking into example that the DigitalOcean project is *example.com* and domains to be updated are

* arecord.example.com
* brecord.example.com
* crecord.zpippo.example.com

The generated jar file, configured with previous shown properties, will be executed as below

```shell
java -jar DO_Record_Updater-0.0.1-SNAPSHOT.jar arecord brecord crecord.zpippo
```

### Run in multi-project mode

To run the application in multiple project mode, it is necessary to explicitly set *use-multi-project* property to *
true*. It is necessary to set the project names list property, multi-project flag and DigitalOcean read/write permission
Bearer Token as following

```properties
config:
    authentication:
        bearer-token:digital_ocean_token
    project:
        names:example.com, example_second.com
```

If those fields are not settled, the application will interrupt the startup.  
Working on command line arguments, the domains to be updated have to be passed to compiled jar file that can be found
into
*target* folder by running the following command

```shell
mvn -U clean package install -DskipTests
```

The command will produce *DO_Record_Updater-0.0.1-SNAPSHOT.jar* file that will be used to run the application. Working
on multi-project mode, it is necessary to specify the specific record to witch project is linked. So, if whe have the
following situation

* example.com
    * pippo.example.com
    * pippo.pluto.example.com
* example_second.com
    * paperino.example_second.com
    * paperino.minny.example_second.com

to specify the record to the specific project, you will use @ into command line argument as example

* pippo@example.com
* paperino@example_second.com
* paperino.minny@example_second.com

The generated jar file, configured with previous shown properties, will be executed as below

```shell
java -jar DO_Record_Updater-0.0.1-SNAPSHOT.jar pippo@example.com paperino@example_second.com paperino.minny@example_second.com
```

## Deploy into Docker

Taking into account that many configurations are similar to [Run in single-project mode](#run-in-single-project-mode)
and [Run in multi-project mode](#run-in-multi-project-mode), the effective changes that have to be done in container
environment is to update *docker-compose.yml* file with *BEARER_TOKEN* and *PROJECT_NAME/PROJECT_NAMES* environment
variables depending on witch mode is going to be deployed, so

* single-project mode &rarr; PROJECT_NAME
* multi-project mode &rarr; PROJECT_NAMES

The *use-multi-project* property needs to be settled from *application.yml* file as below

```properties
config:
    project:
        use-multi-project:false
```

It is also necessary to update [Dockerfile](Dockerfile) adding to entrypoint the records to be updated as below

```shell
ENTRYPOINT ["java", "-jar", "app.jar", "arecord", "brecord", "crecord.zpippo"]
```

or, with multi-project settings

```shell
ENTRYPOINT ["java", "-jar", "app.jar", "pippo@example.com", "pluto@test.net", "paperino@example.com"]
```

To deploy the application use the [deploy.sh](deploy.sh) file that will compile the project and bring up the stack

## Developer References

* [LinkedIn](https://www.linkedin.com/in/antonio-russi-15b915196/)
* [Instagram](https://www.instagram.com/pingmyheart)
* [Mail](mailto:antoniorussi1972@gmail.com)