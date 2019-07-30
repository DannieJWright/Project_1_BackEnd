package com.revature.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HtmlTemplate {

	//makes a back button
	public static void goBack(PrintWriter out) {
		out.println(
				"<hr><input type='button' value='BACK' "
				+ "onclick='goBack()'>"
				+ "<script>"
				+ 	"function goBack() {window.history.back();}"
				+ "</script>"
				);
	}

	//
	public static PrintWriter getHtmlOut (HttpServletResponse res)
		throws IOException {
		
		res.setContentType("text/html");

		return res.getWriter();
	}
	
	public static PrintWriter getJsonOut (HttpServletResponse res)
		throws IOException {
		res.setContentType ("application/json");
		
		return res.getWriter();
	}
	
	public static void printTableHeaders (PrintWriter out, String...headers) {
		
		out.println ("<table border=2px>"
					+ "<tr>");
		
		for (String h : headers) {
			out.println ("<th>" + h + "</th>");
		}
		
		out.println("</tr>");
		
		
	}
}
