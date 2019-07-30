package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.util.HtmlTemplate;

public class GetVsPost extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		String formData = req.getParameter("username");
		
		System.out.println(formData);
		
		PrintWriter out = HtmlTemplate.getHtmlOut(res);
		String values = "";
		
		//going to print all values related to this
		Map<String,String[]> input = req.getParameterMap();
		out.println("<h3>GET</h3>");
		out.println (
				"<table border='2px'<tr><th>PARAMETER</th><th>VALUE</th></tr>"
				);
		
		for (String key : input.keySet()) {
			out.println("<tr><td>" + key + "</td>");
			
			values = "";
			for (String val : input.get(key)) {
				values += (val + " ");
			}
			out.println("<td>" + values + "</td></tr>");
		}
		
		out.println("</table>");
		
		HtmlTemplate.goBack(out);
	}

	
	protected void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		this.doGet(req, res);
	}
}
