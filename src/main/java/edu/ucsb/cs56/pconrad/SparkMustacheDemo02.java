package edu.ucsb.cs56.pconrad;

import static spark.Spark.port;

import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Simple example of using Mustache Templates
 *
 */

public class SparkMustacheDemo02 {

	public static final String CLASSNAME="SparkMustacheDemo02";
	
	public static final Logger log = Logger.getLogger(CLASSNAME);

	public static void main(String[] args) {

        port(getHerokuAssignedPort());
        
        get("/", (rq, rs) -> new ModelAndView(new HashMap(), "home.mustache"), new MustacheTemplateEngine());

        // Get time and purpose to generate key and json
        int key;
        String json;
        get("/create", (request, response) -> {
            String time = request.queryParams("time");
            String purpose = request.queryParams("purpose");
            Map map = new HashMap();
            map.put("time", time);
            map.put("purpose", purpose);
            alarm a = new alarm(time, purpose);
            key = a.hashCode();
            json = a.toJson();
            return new ModelAndView(map, "createresult.mustache");
        }, new MustacheTemplateEngine());
        // send to the web page 
        post("/createresult", (rq, rs) -> key);
		
        // Post to database
        String dbUser = System.getenv().get("MONGODB_USER");
        String dbPassword = System.getenv().get("MONGODB_PASS");
        String dbName  = System.getenv().get("MONGODB__NAME");
        String hostName = System.getenv().get("MONGODB_HOST");
        String request   = "mongodb://" + dbUser + ":" + dbPassword + "@" + hostName + "/" + dbName;
        MongoClientURI uri  = new MongoClientURI(request); 
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> data = db.getCollection("data");
		data.insertOne(new Document("key", key)
                            .append("content", json));

        int key;
        get("/join", (request, response)->{
            key = request.queryParams("key");
            Map map = new HashMap();
            map.put("key", key)
            return new ModelAndView(map, "joinresult.mustache");
        }, new MustacheTemplateEngine());

        MongoCursor<Document> cursor = data.find({ key : {"$eq", key} });
        String content = "not found";
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            content = alarm.toClass(doc.get("content")).toString();
        }
        // send to the web page
        post("/joinresult", (request, response) -> content);
	}
	
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

	
}
