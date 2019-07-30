package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.model.UserAccount;
import com.revature.services.UserService;
import com.revature.utils.HtmlTemplate;

public class RequestHelper {
	
	private static String URI_PREFIX = "/Project_1/reimb";

	private static Logger logger = Logger.getLogger(RequestHelper.class);
	private static ObjectMapper om = new ObjectMapper();
	
	public static void processGet (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		
		final String URI = request.getRequestURI().replace(URI_PREFIX, "");

		System.out.println("tried get :" + URI);
		
		switch (URI) {

		case "/employee/account/logout":
			UserService.invalidateUser(request, response);
		}
	}
	
	
	public static void processPost (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
			
		final String URI = request.getRequestURI().replace(URI_PREFIX, "");
		
		System.out.println("tried post :" + URI);
		
		
		switch (URI) {
		case "/employee/account/create":
		case "/employee/reimbursement/select":
			UserService.selectReimbursementsForAccount(request, response);
			break;
		
		case "/employee/account/login":
			UserService.loginUser(request, response);
			break;
		
		case "/employee/reimbursement/create":
			System.out.println("RequestHelper - going to create reimbursement");
			UserService.insertReimbursement(request, response);
			System.out.println("RequestHelper - done creating reimbursement");
			break;
		
		case "/manager/reimbursement/insert":
		
		case "/manager/reimbursement/select":
			UserService.selectAllReimbursements(request, response);
			break;
			
		case "/manager/reimbursement/update":
			System.out.println("RequestHelper - post - reimbursementUpdate - start");
			UserService.updateReimbursementStatus(request, response);
			System.out.println("RequestHelper - post - reimbursementUpdate - end");
			break;
			
		case "/manager/reimbursement/approve":
			UserService.approveReimbursement(request, response);
			break;
			
		case "/manager/reimbursement/deny":
			UserService.denyReimbursement(request, response);
			break;
			
		}			

	}
		
}
