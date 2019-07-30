package com.revature.model;

import java.util.Arrays;

public class ReimbursementMaker {

	UserAccount user;
	Reimbursement reimbursement;
	byte[] receipt;
	
	
	
	public ReimbursementMaker(UserAccount user, Reimbursement reimbursement, byte[] receipt) {
		super();
		this.user = user;
		this.reimbursement = reimbursement;
		this.receipt = receipt;
	}



	public ReimbursementMaker(Reimbursement reimbursement, byte[] receipt) {
		super();
		this.reimbursement = reimbursement;
		this.receipt = receipt;
	}



	public ReimbursementMaker() {
	}



	public UserAccount getUser() {
		return user;
	}



	public void setUser(UserAccount user) {
		this.user = user;
	}



	public Reimbursement getReimbursement() {
		return reimbursement;
	}



	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}



	public byte[] getReceipt() {
		return receipt;
	}



	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(receipt);
		result = prime * result + ((reimbursement == null) ? 0 : reimbursement.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReimbursementMaker other = (ReimbursementMaker) obj;
		if (!Arrays.equals(receipt, other.receipt))
			return false;
		if (reimbursement == null) {
			if (other.reimbursement != null)
				return false;
		} else if (!reimbursement.equals(other.reimbursement))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "ReimbursementMaker [user=" + user + ", reimbursement=" + reimbursement + ", receipt="
				+ Arrays.toString(receipt) + "]";
	}

	

	
	
}
