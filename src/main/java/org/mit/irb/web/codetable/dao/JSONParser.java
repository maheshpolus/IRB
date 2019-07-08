package org.mit.irb.web.codetable.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class JSONParser {
	protected static Logger logger = Logger.getLogger(JSONParser.class.getName());
	private static final String codeTableJSONPath="/resources/codetable.json";
	
	public static Map<String, Object> getJSONData(HttpServletRequest request) throws Exception{
		ServletContext context = request.getSession().getServletContext();
		InputStream inputStream = context.getResourceAsStream(codeTableJSONPath);
		String codeTableJSON = readFile(inputStream);
		JSONObject jsonObj = new JSONObject(codeTableJSON);
		return jsonObj.toMap();
	}
	
	public static String readFile(InputStream input) throws Exception {
	       BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
	       return buffer.lines().collect(Collectors.joining("\n"));
	        
	    }
}
