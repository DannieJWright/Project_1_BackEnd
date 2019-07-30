package com.revature.main;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.revature.model.UserAccount;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.model.Reimbursement;
import com.revature.model.Reimbursement.ReimbursementType;
import com.revature.model.UserAccount.UserRole;
import com.revature.services.UserService;

public class Driver {
                        
	public Driver() {
		
	}
	
	public static void main (String args[]) {
		UserAccount u = new UserAccount ("Basic", "pass", "First", "last", "email@email.com", UserRole.EMPLOYEE),
					m = new UserAccount ("Manager", "pass", "Boss", "In-Charge", "making@money.com", UserRole.MANAGER);
		Reimbursement r = new Reimbursement (10, "Gas money", null, u, ReimbursementType.OTHER);

		//works
//		UserService.insertAccount(u);
//		UserService.insertAccount(m);
		
		//works
//		UserService.insertReimbursement(u, r);
//		System.out.println(UserService.selectReimbursementsForAccount(u));
//		System.out.println(UserService.selectReimbursements(m));
		
		//works
//		for (Reimbursement temp : UserService.selectReimbursementsForAccount(u)) {
//			if (temp.getStatus() == Reimbursement.ReimbursementStatus.PENDING) {
//				UserService.approveReimbursement(u, temp);
//				break;
//			}
//		}
		
		//works - deprecated
//		UserAccount temp1 = UserService.selectAccount("Basic", "pass"),
//					temp2 = UserService.selectAccount("Manager", "pass");

		ObjectMapper om = new ObjectMapper ();
		String user = "{ \"id\": \"4\", \"name\":\"myname\", \"password\":\"mypass\", \"first_name\":\"Dannie\", \"last_name\":\"Wright\" }";
//		String reimb = "{\"id\":\"5\", \"amount\":\"400\", \"author\":\"5\", \"resolved\":\"null\", \"description\":\"this description\"}";
//				, \"\":\"\", \"\":\"\", \"\":\"\"
		
		try {
			System.out.println(om.readValue(user, UserAccount.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(om.readValue(new File ("C:\\Users\\Dannie\\Desktop\\Revature\\GitLab\\190617jta\\Project_1\\src\\main\\java\\com\\revature\\main\\test.json"), Reimbursement.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		 \"\":\"\", \"\":\"\", \"\":\"\", \"\":\"\"
		
	}

}
