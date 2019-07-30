package com.revature.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.ReimbursementDAO;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementMaker;
import com.revature.model.ReimbursementUpdate;
import com.revature.model.UserAccount;
import com.revature.utils.HtmlTemplate;


public class UserService {
	
	private static Logger logger = Logger.getLogger(UserService.class);
	private static ObjectMapper om = new ObjectMapper();

//	methods of maintaining a session
//	https://www.studytonight.com/servlet/url-rewriting-for-session-management.php
	
	
	
	public static Boolean insertAccount (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {

		UserAccount u = UserService.getAccount(request);
		return new ReimbursementDAO ().insertAccount(u);
	}

	public static UserAccount selectAccount (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		UserAccount u = UserService.getAccount(request);
		String name = u.getName();
		u = new ReimbursementDAO ().selectAccount(u);
		
		if (null == u) {
			logger.warn("Failed to select user account: " + name);
		}
		
		return u;
	}
	
	public static Boolean verifyLogin (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {

		UserAccount u = UserService.getAccount(request);
		String name = u.getName();
		Boolean verify = new ReimbursementDAO ().verifyLogin(u);
		
		if (!verify) {
			logger.warn("Failed to verify user account: " + name);
		}
		
		return verify;
	}
	
	public static List<Reimbursement> selectReimbursementsForAccount (
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		
		UserAccount u = UserService.getAccount(request);

		List<Reimbursement> reimbs = new ReimbursementDAO ().selectReimbursementsForAccount(u);
		
		System.out.println("select reimbs: " + reimbs);
		
		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(reimbs));
		
		return reimbs;
	}
	
	public static List<Reimbursement> selectReimbursementsForAccountByType (
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		Reimbursement.ReimbursementType type = UserService.getType(request); 
		UserAccount u = UserService.getAccount(request);
		return new ReimbursementDAO ().selectReimbursementsForAccount(u, type);
	}
	
	public static List<Reimbursement> selectReimbursementsForAccountByStatus (
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		Reimbursement.ReimbursementStatus status = UserService.getStatus(request); 
		UserAccount u = UserService.getAccount(request);
		return new ReimbursementDAO ().selectReimbursementsForAccount(u, status);
	}
	
	public static Boolean insertReimbursement (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
//		ReimbursementUpdate reimbs = UserService.getAccountAndReimbursements(request);
//		UserAccount u = reimbs.getUser ();
//		ReimbursementDAO dao = new ReimbursementDAO ();
//		
//		
//		for (Reimbursement r : reimbs.getReimbursements()) {
//
//			System.out.println("new reimb: " + r);
//			if (!dao.insertReimbursement(u, r)) {
//				HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(false));
//				return false;
//			}
//		}
//		
//		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(false));
//		return true;
		
		
//		System.out.println(UserService.getBody(request));
		ReimbursementMaker reimb = UserService.getReimbursementMaker(request);
		Reimbursement r = reimb.getReimbursement();
		UserAccount u = reimb.getUser ();
		
		//set the receipt
		r.setReceipt(reimb.getReceipt());
		
		
		System.out.println("new reimb: " + r);
		if (!new ReimbursementDAO ().insertReimbursement(u, r)) {
			HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(false));
			return false;
		}
		
		
		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(false));
		return true;
			
	}
	
	
	
	
	/*
	 * Manager options
	 */
	public static List<Reimbursement> selectAllReimbursements (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		List<Reimbursement> temp = new ReimbursementDAO().selectReimbursements(UserService.getAccount(request));
		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(temp));
		return temp;
	}
	
	public static List<Reimbursement> selectAllReimbursementsByType (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		Reimbursement.ReimbursementType type = UserService.getType(request); 
		UserAccount m = UserService.getAccount(request);
		return new ReimbursementDAO().selectReimbursements(m, type);
	}
	
	public static List<Reimbursement> selectAllReimbursementsByStatus (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		Reimbursement.ReimbursementStatus status = UserService.getStatus(request); 
		UserAccount m = UserService.getAccount(request);
		return new ReimbursementDAO().selectReimbursements(m, status);
	}
	
	public static Boolean updateReimbursementStatus (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
	
		ReimbursementUpdate update = UserService.getAccountAndReimbursements(request);
		UserAccount manager = update.getUser();

		Boolean temp = true;
		for (Reimbursement r : update.getReimbursements()) {
			temp &= new ReimbursementDAO().updateReimbursementStatus(manager, r, r.getStatus());

		}

		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(temp));			
		return temp;
	}
	
	public static Boolean approveReimbursement (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		UserAccount m = UserService.getAccount(request);
		Reimbursement r = UserService.getReimbursement(request);
		Boolean temp = new ReimbursementDAO().updateReimbursementStatus(m, r, Reimbursement.ReimbursementStatus.APPROVED);
		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(temp));
		return temp;
	}
	
	public static Boolean denyReimbursement (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		UserAccount m = UserService.getAccount(request);
		Reimbursement r = UserService.getReimbursement(request);
		Boolean temp = new ReimbursementDAO().updateReimbursementStatus(m, r, Reimbursement.ReimbursementStatus.DENIED);
		HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(temp));
		return temp;
	}

	public static void invalidateUser (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {

		System.out.println("invalidate user - start");
		
		System.out.println("invalidate user - end");
		
	}
	
	//https://www.ibm.com/support/knowledgecenter/en/SSEQTP_8.5.5/com.ibm.websphere.base.doc/ae/cprs_best_practice.html
	public static Boolean loginUser (HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		
//		how to get body of a post request
//		https://stackoverflow.com/questions/8100634/get-the-post-request-body-from-httpservletrequest
		
//		//extract login information from post request;
//		UserAccount u = UserService.getAccount(request);
//		String username = u.getName();
//		
		UserAccount user = null;
		
		System.out.println("Attempt to login");
//		System.out.println("Attempted login for username: " + username);

		if (null != (user = UserService.selectAccount (request, response))) {
			System.out.println("Login instantiated with for username: " + user.getName());
			
			HtmlTemplate.getJsonOut(response).println(om.writeValueAsString(user));
			
			return true;
		}
		else {
			PrintWriter out = HtmlTemplate.getHtmlOut(response);
			
//			System.out.println("Failed login for username: " + username);
			System.out.println("Failed login");
			
			out.println("<h3 style: 'color:red'>Denied (bad login)</h3>");
			HtmlTemplate.goBack(out);
			
			return false;
		}
	}
	
	
	

	
//	public static Map<String,String> getArgs (HttpServletRequest request)
//		throws IOException, ServletException {
//		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//		return JSONParser.stripJSON (body);
//	}
	
	public static String getBody (HttpServletRequest request) throws IOException, ServletException {
		return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}


//	public static Map<String,String> stripOutJSONObject (HttpServletRequest request, String objectKey)
//		throws IOException, ServletException {
//		String body = UserService.getBody(request); //grab request body
//		return JSONParser.stripOutJSONObject(body, objectKey);//strip out json rep for given key		
//	}
	
	public static UserAccount getAccount (HttpServletRequest request) 
		throws IOException, ServletException {
		
//		//strip out json representation for user obj
//		Map<String,String> user = UserService.stripOutJSONObject(request, USER);
//		
//		//parse stripped json representation for user obj
//		return UserAccount.parseString(user);
		
		UserAccount u = om.readValue(UserService.getBody(request), UserAccount.class);
		return u;
	}

	public static Reimbursement getReimbursement (HttpServletRequest request) 
			throws IOException, ServletException {

//		//strip out json representation for user obj
//		Map<String,String> reimb = UserService.stripOutJSONObject(request, REIMB);
//		
//		//parse stripped json rep for reimb obj
//		return Reimbursement.parseString(reimb);
		
		return om.readValue(UserService.getBody(request), Reimbursement.class);
	}
	
	public static ReimbursementUpdate getAccountAndReimbursements (HttpServletRequest request) 
			throws IOException, ServletException {

		return om.readValue(UserService.getBody(request), ReimbursementUpdate.class);
	}
	
	public static ReimbursementMaker getReimbursementMaker (HttpServletRequest request) 
			throws IOException, ServletException {
		
		
		Part reimbPart = request.getPart("reimbursement"),
			 receiptPart = request.getPart("attachment"),
			 authorPart = request.getPart("author");
		
		
		byte[] reimbArr = new byte[(int) reimbPart.getSize()],
				receiptArr = new byte[(int) receiptPart.getSize()],
				authorArr = new byte[(int) authorPart.getSize()];
		
		request.getPart("reimbursement").getInputStream().read(reimbArr);
		Reimbursement reimb = om.readValue(reimbArr, Reimbursement.class);
		
		request.getPart("author").getInputStream().read(authorArr);
		UserAccount user = om.readValue(authorArr, UserAccount.class);
		
		request.getPart("attachment").getInputStream().read(receiptArr);
		
				
		
		return new ReimbursementMaker (user, reimb, receiptArr);
	}
	
	public static Reimbursement.ReimbursementType getType (HttpServletRequest request)
		throws IOException, ServletException {

//		//strip out json representation
//		Map<String,String> type = UserService.stripOutJSONObject(request, TYPE);
//		
//		//parse stripped json representation
//		return Reimbursement.parseType(type.get(TYPE));
		return om.readValue(UserService.getBody(request), Reimbursement.ReimbursementType.class);
	}
	
	public static Reimbursement.ReimbursementStatus getStatus (HttpServletRequest request)
		throws IOException, ServletException {
//		//strip out json representation
//		Map<String,String> status = UserService.stripOutJSONObject(request, STATUS);
//		
//		//parse stripped json representation
//		return Reimbursement.parseStatus(status.get(STATUS));
		return om.readValue(UserService.getBody(request), Reimbursement.ReimbursementStatus.class);
	}
}



