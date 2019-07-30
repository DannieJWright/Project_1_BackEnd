package com.revature.dao;

import static com.revature.utils.CloseStreams.close;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import com.revature.model.Reimbursement;
import com.revature.model.Reimbursement.ReimbursementStatus;
import com.revature.model.Reimbursement.ReimbursementType;
import com.revature.model.UserAccount.UserRole;
import com.revature.model.UserAccount;
import com.revature.utils.ConnectionUtil;

import oracle.jdbc.OracleTypes;

public class ReimbursementDAO implements IReimbursementDAO {
	
	
	private static final String ADD_USER = "{ call addUser(?,?,?,?,?,?) }", //employee_username,pass,first_name,last_name,email,role
						SELECT_USER = "{ call selectUser(?,?,?) }", //employee_username,pass,user cursor
						SELECT_ALL_REIMB = "{ call selectAllReimbursements(?,?,?,?) }", //manager_username,pass,reimbursements cursor, users cursor 
						SELECT_REIMB_BY_STATUS = "{ call selectReimbursementsByStatus(?,?,?,?,?) }",//manager_username,pass,status_id,reimbursements cursor,users cursor
						SELECT_USERS_REIMB = "{ call selectUserReimbursements(?,?,?,?) }",//employee_username,pass,reimbursements cursor, managers cursor
						UPDATE_REIMB = "{ call updateReimbursement(?,?,?,?) }", //manager_username,pass,reimbursement_id,status_id
						APPROVE_REIMB = "{ call approveReimbursement(?,?,?) }", //manager_username,pass,reimbursement_id
						DENY_REIMB = "{ call denyReimbursement(?,?,?) }",//manager_username,pass,reimbursement_id
						ADD_REIMB = "{ call addReimbursement(?,?,?,?,?,?) }"; //employee_username,pass,amount,description,receipt,reimbursement_type
			
	
	private static Logger logger = Logger.getLogger (ReimbursementDAO.class);

	public ReimbursementDAO() {
	}
	
	public Boolean insertAccount (UserAccount user) {
		CallableStatement cs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()) {

//			ADD_USER = "{ call addUser(?,?,?,?,?,?) }", //employee_username,pass,first_name,last_name,email,role
			cs = conn.prepareCall(ADD_USER);
		
			cs.setString(1, user.getName());
			cs.setString(2, user.getPassword());
			cs.setString(3, user.getFirst_name());
			cs.setString(4, user.getLast_name());
			cs.setString(5, user.getEmail());
			cs.setInt(6, user.getRole().ordinal());
			
			cs.execute();
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.info("Failed to add user: " + user.getName());
			
			return false;
		}
		finally {
			close(cs);
		}
		
		logger.info("Succesfully added user: " + user.getName());
		
		return true;
	}

	@Override
	public UserAccount selectAccount(UserAccount u) {
		CallableStatement cs = null;
		ResultSet rs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()) {

			cs = conn.prepareCall(SELECT_USER);

			//"{ call selectUser(?,?,?) }", //employee_username,pass,user cursor
			cs.setString(1, u.getName());
			cs.setString(2, u.getPassword());
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			
			cs.execute();
			
			
			rs = (ResultSet) cs.getObject(3);
			//UserAccount (int id, String name, String password, String first_name, 
			//				String last_name, String email, UserRole role)
			//
			//sql return (ers_users_id,ers_username, user_first_name,
			//user_last_name, user_email,user_role_id)
			if (rs.next()) {
				u = new UserAccount (rs.getInt(1),		//id
									 rs.getString(2), 	//name
									 u.getPassword(),	//pass
									 rs.getString(3),	//first_name
									 rs.getString(4),	//last_name
									 rs.getString(5),	//email
									 UserAccount.UserRole.values()[rs.getInt (6)], //role
									 false //don't hash the password again
									);
				
				logger.info("Succesfully found account with name: " + u.getName());
			}
			else {
				System.out.println("ReimbursementDAO - Failed to login account with username: " +  u.getName());
				return null;
			}
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.warn("ReimbursementDAO - Failed to recieve account with name: " +  u.getName());
			
			return null;
		}
		finally {
			close(cs);
		}
		
		
		return u;
	}

	//may want to change so can get the account info of the approving managers
	@Override
	public List<Reimbursement> selectReimbursementsForAccount(UserAccount u) {
		CallableStatement cs = null;
		ResultSet reimbs = null,
				  mngrs = null;
		List<Reimbursement> r = new ArrayList<Reimbursement> ();
		Map<Integer, UserAccount> managers = new HashMap<Integer,UserAccount> ();
		
		System.out.println("reimbDAO - selectForAccount - before connection");
		
		try (Connection conn = ConnectionUtil.getConnection()) {

			System.out.println("reimbDAO - selectForAccount - after connection");
			
			cs = conn.prepareCall(SELECT_USERS_REIMB);

			//SELECT_USERS_REIMB = "{ call selectUserReimbursements(?,?,?) }",//employee_username,pass,reimbursements cursor
			cs.setString(1, u.getName());
			cs.setString(2, u.getPassword());
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, OracleTypes.CURSOR);
			
			System.out.println("reimbDAO - selectForAccount - before execute");
			
			cs.execute();
			
			System.out.println("reimbDAO - selectForAccount - after execute");
			
			reimbs = (ResultSet) cs.getObject(3);
			mngrs = (ResultSet) cs.getObject(4);
			
			System.out.println("ReimbDAO - selectForAccount - getting managers");
			
			int id;
			while (mngrs.next()) {
				id = mngrs.getInt(1);
				
				managers.put(
						id, new UserAccount (
								id,					//id
								mngrs.getString(2), //username
								mngrs.getString(3),	//first name
								mngrs.getString(4),	//last name
								mngrs.getString(5),	//email
								UserRole.values()[mngrs.getInt(6)] //role
								));
			}
			
			System.out.println("ReimbDAO - selectForAccount - getting reimbursements");
			
			UserAccount resolver = null;
			while (reimbs.next()) {
				//grab manager that approved this reciept
				resolver = managers.get(reimbs.getInt(8));
//				Blob blob = reimbs.getBlob(6);
//				byte[] receipt = null;
//				
//				if (null != blob) {
//					receipt = fromBlob (blob);
//				}
				
				Reimbursement re;	
				r.add( re = new Reimbursement (
						reimbs.getInt (1),	//id
						reimbs.getDouble(2),	//amount
						reimbs.getTimestamp (3),
						reimbs.getTimestamp (4),
						reimbs.getString(5),
//						fromBlob (reimbs.getBlob(6)),
						reimbs.getBytes(6),
						//null,//new File (rs.getBlob(6).getBinaryStream()), //receipt
						u, //author (7)
						resolver, //resolver (8)
						Reimbursement.ReimbursementStatus.values()[reimbs.getInt(9)], //(9)
						Reimbursement.ReimbursementType.values()[reimbs.getInt(10)]
						));
				if (null != re.getReceipt()) {
					System.out.println(re.getReceipt());
				}
			}
			
			System.out.println("ReimbDAO - selectForAccount - finished getting data");
			
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.warn("Failed to find reimbursements for account with name: " + u.getName());
			
			return null;
		}
		finally {
			close(cs);
		}
		
		logger.info("Succesfully found reimbursements for account with name: " + u.getName());
		
		System.out.println(r.toString());
		
		return r;
	}

	@Override
	public List<Reimbursement> selectReimbursementsForAccount(UserAccount user, ReimbursementType type) {
		List<Reimbursement> old = this.selectReimbursementsForAccount(user),
							ret = new ArrayList<Reimbursement> ();
		
		for (Reimbursement r : old) {
			if (r.getType() == type) {
				ret.add(r);
			}
		}
		
		return ret;
	}

	@Override
	public List<Reimbursement> selectReimbursementsForAccount(UserAccount user, ReimbursementStatus status) {
		List<Reimbursement> old = this.selectReimbursementsForAccount(user),
							ret = new ArrayList<Reimbursement> ();

		for (Reimbursement r : old) {
			if (r.getStatus() == status) {
				ret.add(r);
			}
		}
		
		return ret;
	}

	@Override
	public Boolean insertReimbursement(UserAccount user, Reimbursement reimbursiment) {
		CallableStatement cs = null;

		try (Connection conn = ConnectionUtil.getConnection()) {

			cs = conn.prepareCall(ADD_REIMB);

			Blob receipt = null;
			if (null != reimbursiment.getReceipt()) {
//				receipt = new Blob (reimbursiment.getReceipt ());
				
			}
			
			//employee_username,pass,amount,description,receipt,reimbursement_type
			cs.setString(1, user.getName());
			cs.setString(2, user.getPassword());
			cs.setDouble(3, reimbursiment.getAmount());
			cs.setString(4, reimbursiment.getDescription());
//			cs.setBlob(5, receipt);
			cs.setBytes(5, reimbursiment.getReceipt());
//			cs.setBlob(5, fs); 
			cs.setInt(6, reimbursiment.getType().ordinal());
			
			cs.execute();
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.info("Failed to insert reimbursement request by " + user.getName());
			
			return false;
		} 
		finally {
			close(cs);
//			close (fs);
		}
		
		logger.info(user.getName() + " succesfully inserted reimbursement request");
		
		return true;
	}

	
	
	
	
	@Override
	public List<Reimbursement> selectReimbursements(UserAccount manager) {

	
		CallableStatement cs = null;
		ResultSet reimbs = null,
				  users = null;
		List<Reimbursement> r = new ArrayList<Reimbursement> ();
		Map<Integer, UserAccount> employees = new HashMap<Integer,UserAccount> ();
		
		try (Connection conn = ConnectionUtil.getConnection()) {

			cs = conn.prepareCall(SELECT_ALL_REIMB);

//			SELECT_ALL_REIMB = "{ call selectAllReimbursements(?,?,?,?) }", //manager_username,pass,reimbursements cursor, users cursor
			cs.setString(1, manager.getName());
			cs.setString(2, manager.getPassword());
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, OracleTypes.CURSOR);
			
			cs.execute();
			
			reimbs = (ResultSet) cs.getObject(3);
			users = (ResultSet) cs.getObject(4);
			

			int id;
			while (users.next()) {
				id = users.getInt(1);
				
				employees.put(
						id, new UserAccount (
								id,					//id
								users.getString(2), //username
								users.getString(3),	//first name
								users.getString(4),	//last name
								users.getString(5),	//email
								UserRole.values()[users.getInt(6)] //role
								));
			}
			
			UserAccount author = null,
						resolver = null;
			while (reimbs.next()) {
				//grab author of this receipt
				author = employees.get(reimbs.getInt(7));
				//grab manager that approved this receipt
				resolver = employees.get(reimbs.getInt(8));
				
				r.add( new Reimbursement (
						reimbs.getInt (1),	//id
						reimbs.getDouble(2),	//amount
						reimbs.getTimestamp (3),
						reimbs.getTimestamp (4),
						reimbs.getString(5),
//						fromBlob (reimbs.getBlob(6)),
						reimbs.getBytes(6),
						//null,//new File (rs.getBlob(6).getBinaryStream()), //receipt
						author, //author (7)
						resolver, //resolver (8)
						Reimbursement.ReimbursementStatus.values()[reimbs.getInt(9)], //(9)
						Reimbursement.ReimbursementType.values()[reimbs.getInt(10)]
						));
			}
			
			
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.warn("Failed to select reimbursements by manager with name: " + manager.getName());
			
			return null;
		}
		finally {
			close(cs);
		}
		
		logger.info("Succesfully selected reimbursements by manager with name: " + manager.getName());
		
		return r;
		
	}

	@Override
	public List<Reimbursement> selectReimbursements(UserAccount manager, ReimbursementType type) {
		List<Reimbursement> old = this.selectReimbursements(manager),
							ret = new ArrayList<Reimbursement> ();

		for (Reimbursement r : old) {
			if (r.getType() == type) {
				ret.add(r);
			}
		}
		
		return ret;
	}

	@Override
	public List<Reimbursement> selectReimbursements(UserAccount manager, ReimbursementStatus status) {
		List<Reimbursement> old = this.selectReimbursements(manager),
				ret = new ArrayList<Reimbursement> ();

		for (Reimbursement r : old) {
			if (r.getStatus() == status) {
				ret.add(r);
			}
		}

		return ret;
	}

	@Override
	public Boolean updateReimbursementStatus(UserAccount manager, Reimbursement reimbursement,
			ReimbursementStatus status) {
		/*
		updateReimbursement (
	    username IN VARCHAR2,
	    pass IN VARCHAR2,
	    reim_id IN NUMBER,
	    status_id IN NUMBER)
		 */
		
		CallableStatement cs = null;
		FileInputStream fs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()) {

			cs = conn.prepareCall(UPDATE_REIMB);
			
//			UPDATE_REIMB = "{ call updateReimbursement(?,?,?,?) }", //manager_username,pass,reimbursement_id,status_id
			cs.setString(1, manager.getName());
			cs.setString(2, manager.getPassword());
			cs.setInt(3, reimbursement.getId());
			cs.setInt(4, status.ordinal());
			
			cs.execute();
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.info("Failed to approve reimbursement id#: " + reimbursement.getId() + " by manager: " + manager.getName());
			
			return false;
		} 
		finally {
			close(cs);
			close (fs);
		}
		
		logger.info(manager.getName() + " succesfully inserted reimbursement request");
		
		return true;
	}

	
	private byte[] fromBlob (Blob b) {
		
		byte[] receipt = null;
		
		if (null != b) {
			try {
				b.getBinaryStream().read(receipt);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return receipt;
	}
}
