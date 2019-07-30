package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * NEVER DO SUCH OUTRAGOUS STUFF
	 */
	
	
	
	
	/*
	 * Servlets have three key functions in their life cycle:
	 * (don't override these)
	 * 
	 * 	- init() 	- instantiate the servlet if it isn't already (singleton)
	 * 				- if servlet is instantiated already, this method is skipped
	 * 
	 * 
	 * 	- service() - used for the business logic and populating 
	 * 
	 * 
	 * 	- destroy() - 
	 */
	
	
	public void init () throws ServletException {
		System.out.println(this.getServletName() + "  : INSTANTIATED!!!");
		super.init();
	}
	
	/**
	 * Normally NEVER EVER override the service func, unless you really know what you're doing
	 */
	public void service (HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		System.out.println(this.getServletName() + "  : SERVICE CALLED!!!");
		
		/*
		 * In order to populate the response, we need to write to the response.
		 * This is done by using the response's own PrintWriter obj.
		 * (this is step 4.b)
		 */
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		/*
		 * now we send text representing the "hello world" that we want to display on our front end
		 * (this is our response)
		 */
		out.println ("<h3 style='color:green'>Hellow........ I guess</h3>");
		
		/**
		 * Add a back button
		 */
		out.println(
				"<hr><input type='button' value='BACK' "
				+ "onclick='goBack()'>"
				+ "<script>"
				+ 	"function goBack() {window.history.back();}"
				+ "</script>"
				);
		//at the end of service() response and request are sent back to WC
	}

	
	public void destroy () {
		System.out.println(this.getServletName() + "  : Destroy is Called!");
		super.destroy();
	}
	
}












