# Spring-Boot and Camel XML QuickStart

This example demonstrates a Spring Boot example running a Camel REST route connecting to a PostgreSQL database.

The application utilizes the Spring [`@ImportResource`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ImportResource.html) annotation to load a Camel Context definition via a [camel-context.xml](src/main/resources/spring/camel-context.xml) file on the classpath.

### Building

The example can be built with

    mvn clean install

### Running the example in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.9/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)

#### Login as developer 
    ~/Projects/git/beer-api on master*

    $ oc login -u developer -p developer
    Login successful.

    You don't have any projects. You can try to create a new project, by running

    oc new-project <projectname>    

#### Create project beer

    $ oc new-project beer
    Now using project "beer" on server "https://192.168.64.29:8443".

    You can add applications to this project with the 'new-app' command. For example, try:

        oc new-app centos/ruby-22-centos7~https://github.com/openshift/ruby-ex.git

    to build a new example application in Ruby.

#### Setup PostgreSQL database

- Go to the OpenShift Web Console 

- Select the beer project previously created

- Add a PostgreSQL database 
with the following settings:

    - Database service name: postgresql
    - PostgreSQL Connection Username: beeruser
    - PostgreSQL Connection Password: beerpw
    - PostgreSQL Database Name: beerdb  

    $ oc get pods | grep postgresql
    
    postgresql-1-qjq75   1/1       Running   0          1m

- Copy data files into the pod

    $ oc cp postgres/breweries.csv postgresql-1-qjq75:/tmp

    $ oc cp postgres/beers.csv postgresql-1-qjq75:/tmp

    oc cp postgres/*.sql postgresql-1-qjq75:/tmp
-     
    $ oc rsh postgresql-1-qjq75
    sh-4.2$ ls /tmp

    beers.csv  breweries.csv  ks-script-hE5IPf  openbeer-psql.sql  yum.log
    
    sh-4.2$ psql -d beerdb -f /tmp/openbeer-psql.sql -W
    Password: postgres
    CREATE TABLE
    COPY 1414
    CREATE TABLE
    COPY 5901
    
    sh-4.2$ psql -d beerdb
    beerdb=# grant all privileges on all tables in schema public to beeruser;
    GRANT
    
    beerdb=# \q

    sh-4.2$ exit


    

#### SpringBoot Application
The example can be built and run on OpenShift using a single goal:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the OpenShift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the running pods, and view logs and much more.





