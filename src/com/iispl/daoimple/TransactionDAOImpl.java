package com.iispl.daoimple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iispl.dao.TransactionDao;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class TransactionDAOImpl implements TransactionDao {
	private static final String CHECK_TRANSACTION =
	        "SELECT COUNT(*) FROM bank_transaction WHERE transaction_id = ?";
	private static final String INSERT_TRANSACTION =
	        "INSERT INTO bank_transaction "
	      + "(transaction_id,batch_id,from_account,to_account,"
	      + "transaction_type,amount,transaction_date,"
	      + "transaction_status,failure_reason,source_file)"
	      + " VALUES (?,?,?,?,?,?,?,?,?,?)";
	@Override
	public boolean transactionExists(Connection connection, String transactionId) {
		// TODO Auto-generated method stub
		 try (PreparedStatement ps =connection.prepareStatement(CHECK_TRANSACTION)) {

		        ps.setString(1, transactionId);

		        ResultSet rs = ps.executeQuery();
		        if (rs.next()) {

		            return rs.getInt(1) > 0;
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return false;
	}

	@Override
	public boolean saveTransaction(Connection connection, TransactionRequest request, TransactionResult result) {
		// TODO Auto-generated method stub
		try (PreparedStatement ps =
	            connection.prepareStatement(INSERT_TRANSACTION)) {
	        ps.setString(1, request.getTransactionId());
	        ps.setString(2, "BATCH001");
	        ps.setString(3, request.getFromAccount());
	        ps.setString(4, request.getToAccount());
	        ps.setString(5,request.getTransactionType().name());
	        ps.setBigDecimal(6,request.getAmount());
	        ps.setDate(7,java.sql.Date.valueOf(request.getTransactionDate()));
	        ps.setString(8,result.getStatus().name());
	        ps.setString(9,result.getFailureReason());
	        // temporary value
	        ps.setString(10, "INPUT.xml");
	        int rows = ps.executeUpdate();


	        return rows > 0;


	    } catch(SQLException e) {

	        e.printStackTrace();

	    }

	    return false;
	}

	
	}


