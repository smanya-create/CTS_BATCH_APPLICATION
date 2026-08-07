package com.iispl.daoimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iispl.dao.AccountDao;
import com.iispl.enums.AccountStatus;
import com.iispl.model.Account;

public class AccountDAOImpl implements AccountDao {
	private static final String FIND_ACCOUNT =
	        "SELECT * FROM account WHERE account_number = ?";
	private static final String DEBIT_ACCOUNT="UPDATE account " +
		    "SET balance = balance - ? " +
		    "WHERE account_number = ? " +
		    "AND account_status = 'ACTIVE' " +
		    "AND balance >= ?";
	private static final String CREDIT_ACCOUNT="UPDATE account " +
		    "SET balance = balance + ? " +
		    "WHERE account_number = ? " +
		    "AND account_status = 'ACTIVE' " ;
	@Override
	public Account findAccountByNumber(Connection connection, String accountNumber) {
		// TODO Auto-generated method stub
		try(PreparedStatement preparesmt=connection.prepareStatement(FIND_ACCOUNT)){
			preparesmt.setString(1, accountNumber);
			ResultSet resultSet=preparesmt.executeQuery();
			if(resultSet.next()) {
				return new Account(resultSet.getString("account_number"),resultSet.getString("customer_name"),resultSet.getBigDecimal("balance"),AccountStatus.valueOf(resultSet.getString("account_status"))
						);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean debitAccount(Connection connection, String accountNumber, BigDecimal amount) {
		// TODO Auto-generated method stub
		 try (PreparedStatement ps =
		            connection.prepareStatement(DEBIT_ACCOUNT)) {

		        ps.setBigDecimal(1, amount);
		        ps.setString(2, accountNumber);
		        ps.setBigDecimal(3, amount);

		        int rowsUpdated = ps.executeUpdate();

		        return rowsUpdated > 0;

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return false;
	}

	@Override
	public boolean creditAccount(Connection connection, String accountNumber, BigDecimal amount) {
		// TODO Auto-generated method stub
		try (PreparedStatement ps =
	            connection.prepareStatement(CREDIT_ACCOUNT)) {

	        ps.setBigDecimal(1, amount);
	        ps.setString(2, accountNumber);

	        int rowsUpdated = ps.executeUpdate();

	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	

}
