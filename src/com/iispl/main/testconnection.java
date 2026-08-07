package com.iispl.main;

import java.sql.SQLException;

import com.iispl.util.DBUtil;

public class testconnection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
			DBUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
