package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

  private static Connection conn = null;

  public static Connection getConnection () {

	  
    try {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		String []creds = System.getenv("DBProperties_Reimbursement").split(";");
		
		conn = DriverManager.getConnection(creds[Credentials.ENDPOINT.ordinal()],
                                        creds[Credentials.USERNAME.ordinal()],
                                        creds[Credentials.PASSWORD.ordinal()]);
    }
    catch (SQLException e) {
    	System.out.println("Failed to connect");
      e.printStackTrace();
      return null;
    }

    return conn;
  }

  enum Credentials {
    ENDPOINT,
    USERNAME,
    PASSWORD
  }

}
