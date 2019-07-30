package com.revature.model;

import java.util.Map;

public class UserAccount {

	public enum UserRole {
		MANAGER,
		EMPLOYEE
	}
	
	private static final String ID = "id",
								 NAME = "username",
								 PASS = "pass",
								 FIRST = "first_name",
								 LAST = "last_name",
								 EMAIL = "email",
								 ROLE = "role";
	
	private Integer id;
	private String name,
			password,
			first_name,
			last_name,
			email;
	private UserRole role = UserRole.EMPLOYEE;
	

	public static UserAccount parseString (Map<String,String> vals) {

		Integer id = null;
		try {
			id = Integer.parseInt(vals.get(ID));
		}
		catch (Exception e) {}
		
		String name = vals.get(NAME),
				pass = vals.get(PASS),
				first_name = vals.get(FIRST),
				last_name = vals.get(LAST),
				email = vals.get(EMAIL);
		
		UserRole role = UserRole.EMPLOYEE;;
		try {
			role = UserRole.values()[Integer.parseInt(vals.get(ROLE))];
		}
		catch (Exception e) {}

		return new UserAccount (id, name, pass, first_name, last_name, email, role);
	}
	
	public UserAccount() {
	}



	public UserAccount(int id, String name, String first_name, 
			String last_name, String email, UserRole role) {
		super();
		this.setId(id);
		this.name = name;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role = role;
	}
	
	public UserAccount(int id, String name, String password, String first_name, 
			String last_name, String email, UserRole role) {
		super();
		this.setId(id);
		this.name = name;
		this.password = Integer.toString(password.hashCode());
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role = role;
	}
	
	public UserAccount(int id, String name, String password, String first_name, 
			String last_name, String email, UserRole role, boolean bHashPass) {
		super();
		this.setId(id);
		this.name = name;
		if (bHashPass) {
			this.password = Integer.toString(password.hashCode());
		}
		else {
			this.password = password;
		}
		
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role = role;
	}

	public UserAccount (String name, String password) {
		super();
		this.name = name;
		this.password = Integer.toString(password.hashCode());
	}
	
	public UserAccount(String name, String password, String first_name,
			String last_name, String email, UserRole role) {
		super();
		this.setId(-1);
		this.name = name;
		this.password = Integer.toString(password.hashCode());
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role = role;
	}
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		if (null != password) {
			this.password = String.valueOf(password.hashCode());
		}
		else {
			this.password = null;
		}
	}



	public String getFirst_name() {
		return first_name;
	}



	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}



	public String getLast_name() {
		return last_name;
	}



	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public UserRole getRole() {
		return role;
	}



	public void setRole(UserRole role) {
		this.role = role;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((first_name == null) ? 0 : first_name.hashCode());
		result = prime * result + ((last_name == null) ? 0 : last_name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		UserAccount other = (UserAccount) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (first_name == null) {
			if (other.first_name != null)
				return false;
		} else if (!first_name.equals(other.first_name))
			return false;
		if (last_name == null) {
			if (other.last_name != null)
				return false;
		} else if (!last_name.equals(other.last_name))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "UserAccount [name=" + name + ", password=" + password + ", first_name=" + first_name + ", last_name="
				+ last_name + ", email=" + email + ", role=" + role + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	
}
