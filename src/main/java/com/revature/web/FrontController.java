package com.revature.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class FrontController
 */
@MultipartConfig
public class FrontController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(FrontController.class);
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("do get!!!!!!");
		RequestHelper.processGet(request, response);
		System.out.println("end get!!!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("do post!!!!");
		RequestHelper.processPost(request, response);
		System.out.println("end post!!!!");
	}

}
