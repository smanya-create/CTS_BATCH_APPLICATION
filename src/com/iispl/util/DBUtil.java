package com.iispl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static final String URL =
            "jdbc:postgresql://localhost:5432/cts_db";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            "postgres";


    public static Connection getConnection() throws SQLException {


    	 try {
             Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             System.out.println("Database Connected Successfully");
             return con;

         } catch (Exception e) {
             e.printStackTrace();
         }

         return null;

    }
	

}
