# metaclerk
*metaclerk* is database metadata comparison tool. The main purpose of this tool is to check database schema before
running database installation/upgrade scripts. Nowadays DevOps practices are very popular and database schema 
installations and upgrades are performed using various automation tools. Installation automation is very useful 
because it performs exactly the same database installation procedure and scripts on all application environments. 
Performing exactly the same installation procedure and using the same installation scripts on all environments we
 expect that 
database schemas are exactly the same in all environments. But in practice because of various unforeseen situations 
database schemas have small differences, 
 e.g. DBA made tiny change in production and forgot to inform development team. Those changes collect over time and 
 one "sunny" day database installation script crashes because of 
 significant difference among production and testing/development environments. All of you know how painful those 
 crashes are. It is very easy to rollback broken web application to previous version, but it is very hard and time 
 consuming to rollback database to previous state or to find, fix and update installation scripts in order 
 to successfully finish  database installation.  
  
 Therefore I developed *metaclerk* in order to minimize risk of having differences in database schemas across different 
 environments. It is intended to use *metaclerk* with automated installation tools to check that database schema is
  in expected state before executing installation script. Also I recommend to use this tool after installation scripts 
  execution to 
  test that database has reached expected state. And of course *metaclerk* can be used to compare two database schemas 
  manually to find exact differences. *metaclerk* code can be integrated into your application to check that database 
  schema is in expected state on application startup.    

At the moment *metaclerk* works with a small amount of database schema metadata. Metadata of tables, views and 
grants to other database schemas are used. Nevertheless the biggest amount of troubles rises from differences in these 
metadata objects. 
  
Program usage is very simple - *metaclerk* is a command line tool with two commands:

1. *Snapshot* command is used to capture current database schema snapshot and output captured data in xml format.
2. *Check* command is used to check captured snapshot metadata against current database metadata. 
 
Please read [*'Program usage examples'*](#programExamples) section for program usage documentation and examples. 

##Build environment setup and program build

In order to build *metaclerk* program from source code you must follow these steps:

1. Install and setup Java 8 JDK.
2. Install and setup Maven. I am using maven version 3.0.4 for this project. You can use different version if you wish.
3. Clone *metaclerk* project to your computer from GitHub using URL: https://github.com/rjaskovikas/metaclerk.git. 
Directory where you cloned *metaclerk* source I will call *&lt;metaclerk-src-dir&gt;* in following description. 
4. Download Oracle JDBC driver version 11.2.0.4 ([link to: ojdbc6.jar](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html)) 
and copy downloaded ojdbc6.jar file to *&lt;metaclerk-src-dir&gt;/libs-to-install* directory. 
5. Open terminal in linux or command prompt in windows and go to *&lt;metaclerk-src-dir&gt;/libs-to-install* directory. 
Execute *install* script. This should install Oracle JDBC driver in local maven repository.
6. Using the same terminal go to *&lt;metaclerk-src-dir&gt;* and execute "*mvn package*" command.
7. Go to *&lt;metaclerk-src-dir&gt;/executable* directory and execute *metaclerk* command. You should get output similar to this:
```bash
C:\metaclerk\executable>metaclerk  
Program usage:  
metaclerk [command] {command parameters}  
 
Commands:  
        snapshot - prints database schema snapshot  
        check - checks database schema against database snapshot
```
8. Copy content of *&lt;metaclerk-src-dir&gt;/executable* directory to your desired location and use it.

##<a name="programExamples"/>Program usage examples
Best program documentation is a list of examples of program usage. Bellow I provided a list of main examples of 
program usage. 
In case you need more information about commands parameter please execute '*metaclerk [command] help*'. 

### Making database snapshots
  
* Make mysql database dev_db.lan snapshot and output it to console  
```bash
> metaclerk snapshot -d mysql -c "jdbc:mysql://dev_db.lan?useSSL=false"  -u testUser -p testUserPassword -s testSchema
```
* Make postgresql server dev_db.lan database db_name snapshot and ouput it to console
```bash
> metaclerk snapshot -d postgresql -c "jdbc:postgresql://dev_db.lan/db_name"  -u testUser -p testUserPassword -s testSchema
```
 
* Make oracle database *dev_db.lan* snapshot and save it to file *db-snapshot-release-3.xml*

```bash
> metaclerk snapshot -d oracle -c "jdbc:oracle:thin:@dev_db.lan:1521:service_sid" -u testUser -p testUserPassword -s testSchema -o db-snapshot-release-3.xml
```

or
```bash
> metaclerk snapshot -c "jdbc:oracle:thin:@dev_db.lan:1521:service_sid" -u testUser -p testUserPassword -s testSchema -o db-snapshot-release-3.xml
```

__Note:__ you can skip "*-d oracle*" parameter because oracle plugin is a default.
  
### Comparing database to snapshots
When *metaclerk* executes *check* command it returns *true* value to shell if snapshot is equal to database and *false*
 otherwise. 
 *check* command doesn't provide any information about differences in schemas by default, it just returns *true* or
  *false* values.
If you need information about objects' differences execute *check* command with *-v* parameter. If you need full 
information about differences including object and their attributes execute *check* command with *-vv* parameter.

* Compare oracle database *dev_db.lan* to snapshot file *db-snapshot-release-3.xml* using very verbose output mode
```bash
> metaclerk.cmd check -d oracle -c "jdbc:oracle:thin:@dev_db.lan:1521:service_sid" -u testUser -p testUserPassword -s testSchema -i db-snapshot-release-3.xml -vv
```

* Compare mysql database *test_db.lan* to snapshot file *db-snapshot-release-3.xml* using ignore list *ignore-list.xml* file. 
```bash
> metaclerk.cmd check -d mysql -c "jdbc:mysql://dev_db.lan?useSSL=false" -u testUser -p testUserPassword -s testSchema -i db-snapshot-release-3.xml -il ignore-list.xml
``` 

__Note__: Use ignore list file in order to tell *metaclerk* to ignore some tables or views while comparing database
 with snapshot. 
           Tables or views mentioned in ignore list are ignored in both snapshot and database schemas. 
           Please see ["Ignore list file structure"](#ignoreListFile) section for ignore list file format.
 
### Comparing two databases
By default *metaclerk* outputs snapshot data to console when makes snapshot and reads snapshot data from console input
 when compares snapshot to database. Such behaviour is very useful in order to compare two databases without producing 
 additional 
snapshot files. Bellow is an example of development database *dev_db.lan* comparision against 
test database *test_db.lan* using shell pipe.
```bash
> metaclerk snapshot -d mysql -c "jdbc:mysql://dev_db.lan?useSSL=false"  -u testUser -p password -s develSchema | metaclerk.cmd check -d mysql -c "jdbc:mysql://test_db.lan?useSSL=false" -u testUser -p password -s testSchema -vv
```

###Database users with empty passwords
*metaclerk* treats database user password as mandatory parameter, but sometimes developers 
use database users with empty passwords in development environment. Bellow is an example of *metaclerk* invocation 
with empty *root* user password.  
```bash
> metaclerk snapshot -d mysql -c "jdbc:mysql://dev_db.lan?useSSL=false" -u root -p "" -s testSchema
```

##Extending *metaclerk* to support other databases
*metaclerk* supports Oracle, PostgreSQL and MySql databases. Database support is not hardcoded into application, 
but instead database support is provided using database plugins. *metaclerk* architecture allows you to write your own
database plugin and you can use unlimited number of databases plugins.   
For database plugin example, please see *&lt;metaclerk-src-dir&gt;/mysql-plugin* or *&lt;metaclerk-src-dir&gt;/oracle-plugin* 
modules implementation.  
Small note on *metaclerk* plugin loading procedure: *metaclerk* tries to load plugin which name is provided with *'-d'*
 parameter. For example: *metaclerk* plugin loading class looks for 
 *'org.rola.metaclerk.plugnin.__mysql__.DaoPluginFactory'* class if *'-d mysql'* parameter is provided. 
  Plugin name in package name should be written in lower case otherwise *metaclerk* will never find it. 
  Plugin name provided 
  with *'-d'* parameter executing *metaclerk* is case insensitive i.e. providing *'-d MySQL*, *'-d mySql'* and 
  *'-d mysql'* has no difference on plugin lookup. In all three cases *metaclerk* will look for 
  *'org.rola.metaclerk.plugnin.__mysql__.DaoPluginFactory'* class.      

##<a name="ignoreListFile"/>Ignore list file structure
Ignore list file is a simple xml file. At the moment both tables and views that should be ignored 
must be listed in *&lt;tables2ignore&gt;* tag scope using *&lt;table&gt;* tags as shown in example bellow.
```xml
<?xml version="1.0"?>
<tables2ignore>
    <table name="table_name_to_ignore"/>
    <table name="view_name_to_ignore"/>
</tables2ignore>"
```

