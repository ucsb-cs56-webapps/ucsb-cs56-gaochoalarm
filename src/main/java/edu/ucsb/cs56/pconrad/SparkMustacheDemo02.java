package edu.ucsb.cs56.pconrad;

import static spark.Spark.port;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class SparkMustacheDemo02 {

	public static final String CLASSNAME="SparkMustacheDemo02";
	
	public static final Logger log = Logger.getLogger(CLASSNAME);

	public static void main(String[] args) {

        port(getHerokuAssignedPort());
        
        get("/", (rq, rs) -> new ModelAndView(new HashMap(), "home.mustache"), new MustacheTemplateEngine());

        // Get time and purpose to generate key and json
        int createKey;
        String json;
        get("/create", (request, response) -> {
            String time = request.queryParams("time");
            String purpose = request.queryParams("purpose");
            Map map = new HashMap();
            map.put("time", time);
            map.put("purpose", purpose);
            alarm a = new alarm(time, purpose);
            createKey = a.hashCode();
            json = a.toJson();
            return new ModelAndView(map, "createresult.mustache");
        }, new MustacheTemplateEngine());
        // send to the web page 
        post("/createresult", (request, response) -> createKey.toString());
		
        // Post to database
        String dbUser = System.getenv().get("MONGODB_USER");
        String dbPassword = System.getenv().get("MONGODB_PASS");
        String dbName = System.getenv().get("MONGODB__NAME");
        String hostName = System.getenv().get("MONGODB_HOST");
        String request = "mongodb://" + dbUser + ":" + dbPassword + "@" + hostName + "/" + dbName;
        MongoClientURI uri  = new MongoClientURI(request); 
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> data = db.getCollection("data");
		data.insertOne(new Document("key", createKey)
                            .append("content", json));

        int key;
        get("/join", (request, response) -> {
            key = request.queryParams("key");
            Map map = new HashMap();
            map.put("key", key);
            return new ModelAndView(map, "joinresult.mustache");
        }, new MustacheTemplateEngine());

        Document doc = data.find(eq("key", key)).first();
        String content = (doc == null) ? "not found" : alarm.toClass(doc.get("content")).toString();
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
