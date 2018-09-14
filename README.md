# ucsb-cs56-gauchoalarm
https://cs56-m18-gauchoalarm.herokuapp.com/
An App that shares schedule notifications with friends

The homepage of the webapp contains the introduction of GauchoAlarm, and the instruction of how to use our webapp.

In the navigation bar at the top, there are two tabs: "Creare Alarm" and "Join Alarm"

# How to run GauchoAlarm
Start by clicking on "Create Alarm" at the top.
Users can set the date, time and purpose of the alarm. Then the system will automatically return a unique key.
After recieving the unique key that GauchoAlarm generates, you may send your code to your friends,
so that they will be alerted by the alarm to hang out. 

If you have a code from someone who has made an alarm then click on "Join Alarm".
Then, input the unique key given to you. ( i.e. 8914223145) 

# How to Run Source Code: Set up Database through MongoDB
# 1. Set up MLab Account

- Create a free mLab account at [mLab.com](https://mlab.com)
- Create a new deployment with any hosting service
- Click on your new deployment
- Go the users tab and make a new user
- Take note of the user credentials your just made, your db name, and host

```
mongodb://<dbuser>:<dbpassword>@d<dbhost>/<dbname>
```

# 2. Creating environment variables locally
Since we're going to be logging into a remote database, we have to hide our login credentials from the outside world. To do this, we're going to make a ```.env``` file inside our project directory. It should have a structure similar to this.
```
export MONGODB_USER= <your user here> (for example: JohnKennedy)
export MONGODB_PASS=<your pass here> (for example: 1243541f)
export MONGODB_NAME=<your database name here> (for example: alarm)
export MONGODB_HOST=<your host here> (for example: ds027583.mlab.com)
export MONGODB_PORT=<your port here> (for example: 27583)
```
__!!! IMPORTANT !!!__
Make sure to run the following to add ```.env``` to your .gitignore! This way our secret credentials won't be pushed into our GitHub repos.
```
echo ".env" >> .gitignore
```
# 3. Additions to pom.xml
If you forked the project, you must add the following lines to your pom.xml inside the ```<dependencies>``` tag.
```XML
<!-- MongoDB NoSQL Database -->
 <dependencies>
    ...
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongo-java-driver</artifactId>
        <version>3.8.1</version>
    </dependency>
    ...
</dependencies>
```

# To use

| To do this | Do this |
| -----------|-----------|
| run the program | Type `mvn exec:java`.  Visit the web page it indicates in the message |
| check that edits to the pom.xml file are valid | Type `mvn validate` |
| clean up so you can recompile everything  | Type `mvn clean` |
| edit the source code for the app | edit files in `src/main/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| edit the source code for the app | edit files in `src/test/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| compile    | Type `mvn compile` |
| run junit tests | Type `mvn test` |
| build the website, including javadoc | Type `mvn site-deploy` then look in either `target/site/apidocs/index.html`  |
| copy the website to `/docs` for publishing via github-pages | Type `mvn site-deploy` then look for javadoc in `docs/apidocs/index.html` |	
| make a jar file | Type `mvn package` and look in `target/*.jar` |

| run the main in the jar file | Type `java -jar target/spark-mustache-demo-02-1.0-jar-with-dependencies.jar ` |
| change which main gets run by the jar | Edit the `<mainClass>` element in `pom.xml` |
| deploy the project on Heroku | Type `mvn package heroku:deploy` |

# Future expectation
- login/logout via social application (i.e. Facebook, Google, GitHub...)
- sign up for Gauchoalarm, user profile
- set the alarm directly into the calender
