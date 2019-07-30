package com.revature.model;

import java.util.Arrays;

public class ReimbursementUpdate {

	UserAccount user;
	Reimbursement []reimbursements;
	
	public ReimbursementUpdate() {
	}

	public ReimbursementUpdate(UserAccount user, Reimbursement[] reimbursements) {
		super();
		this.user = user;
		this.reimbursements = reimbursements;
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Reimbursement[] getReimbursements() {
		return reimbursements;
	}

	public void setReimbursements(Reimbursement[] reimbursements) {
		this.reimbursements = reimbursements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + Arrays.hashCode(reimbursements);
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
		ReimbursementUpdate other = (ReimbursementUpdate) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (!Arrays.equals(reimbursements, other.reimbursements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReimbursementUpdate [user=" + user + ", reimbursements=" + Arrays.toString(reimbursements) + "]";
	}

}
