package com.revature.dao;

import java.util.List;

import com.revature.model.Reimbursement;
import com.revature.model.UserAccount;

public interface IReimbursementDAO {
	
	/*
	 * Regular employee options
	 */
	public Boolean insertAccount (UserAccount u);
	public UserAccount selectAccount (UserAccount u);
	default public Boolean verifyLogin (UserAccount u) {
		return null != this.selectAccount(u);
	}
	
	public List<Reimbursement> selectReimbursementsForAccount (UserAccount user);
	public List<Reimbursement> selectReimbursementsForAccount (UserAccount user, Reimbursement.ReimbursementType type);
	public List<Reimbursement> selectReimbursementsForAccount (UserAccount user, Reimbursement.ReimbursementStatus status);
	public Boolean insertReimbursement (UserAccount user, Reimbursement reimbursiment);
	
	
	
	
	/*
	 * Manager options
	 */
	public List<Reimbursement> selectReimbursements (UserAccount manager);
	public List<Reimbursement> selectReimbursements (UserAccount manager, Reimbursement.ReimbursementType type);
	public List<Reimbursement> selectReimbursements (UserAccount manager, Reimbursement.ReimbursementStatus status);
	
	public Boolean updateReimbursementStatus (UserAccount manager, Reimbursement reimbursement, Reimbursement.ReimbursementStatus status);
	default public Boolean approveReimbursement (UserAccount manager, Reimbursement reimbursement) {
		return this.updateReimbursementStatus(manager, reimbursement, Reimbursement.ReimbursementStatus.APPROVED);
	}
	default public Boolean denyReimbursement (UserAccount manager, Reimbursement reimbursement) {
		return this.updateReimbursementStatus(manager, reimbursement, Reimbursement.ReimbursementStatus.DENIED);
	}
	
	
}
