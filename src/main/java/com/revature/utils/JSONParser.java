package com.revature.utils;


import java.util.HashMap;
import java.util.Map;

public class JSONParser {
	
	
	public static Map<String,String> stripJSON (String input) {
		
		Map<String,String> output = new HashMap<String,String> ();
		
		input = input.replace("{", "").replace("}", "");
		
		try {
			String[] temp = input.split(","); //separate out the key/value pairs
			for (String str : temp) {
				String[] keyvalue = str.split(":"); //extract the key and value
				
				for (int i = 0; i < keyvalue.length; i += 2) {
					output.put(keyvalue[i].replace("\"", "").intern(),	keyvalue[i + 1].replace("\"", ""));
				}
			}
		}
		catch (Exception e) {
			System.out.println("invalid JSON information: " + input);
			e.printStackTrace();
		}
		
		return output;
	}
	//{ "user":"{ "username }" }
	
	public static Map<String,String> stripOutJSONObject (String input, String objectKey)  {

		//grab out desired object from the input
		Map<String,	String> tags = JSONParser.stripJSON(input);
		String desiredObj = tags.get(objectKey);
		
		//strip out JSON for the desired object
		return JSONParser.stripJSON(desiredObj);
	}

}




