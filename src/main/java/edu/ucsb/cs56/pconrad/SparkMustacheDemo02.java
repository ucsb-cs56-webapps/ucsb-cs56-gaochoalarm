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
		
		Map map = new HashMap();
        map.put("name", "Sam");
		
        // hello.mustache file is in resources/templates directory
        get("/", (rq, rs) -> new ModelAndView(map, "home.mustache"), new MustacheTemplateEngine());

		get("/create", (rq, rs) -> new ModelAndView(map, "create.mustache"), new MustacheTemplateEngine());
        get("/join", (rq, rs) -> new ModelAndView(map, "join.mustache"), new MustacheTemplateEngine());
		post("/createresult", (rq, rs) -> new ModelAndView(map, "createresult.mustache"), new MustacheTemplateEngine());
		get("/joinresult", (rq, rs) -> new ModelAndView(map, "joinresult.mustache"), new MustacheTemplateEngine());
	}
	
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

	
}
