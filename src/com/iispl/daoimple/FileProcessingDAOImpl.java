package com.iispl.daoimple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.iispl.dao.FileDaoProcessing;
import com.iispl.model.FileProcessingSummary;

public class FileProcessingDAOImpl implements FileDaoProcessing {

	private static final String INSERT_SUMMARY =
            "INSERT INTO file_processing "
          + "(batch_id,file_name,total_records,"
          + "successful_records,failed_records,"
          + "processing_status)"
          + " VALUES (?,?,?,?,?,?)";


    @Override
    public boolean saveSummary(Connection connection,
                               FileProcessingSummary summary) {


        try(PreparedStatement ps =connection.prepareStatement(INSERT_SUMMARY)) {
        	String batchId = "BATCH_" + System.currentTimeMillis();
        	ps.setString(1, batchId);
            ps.setString(2,summary.getFileName());
            ps.setInt(3,summary.getTotalTransactions());
            ps.setInt(4,summary.getSuccessCount());
            ps.setInt(5,summary.getFailureCount());
            ps.setString(6,summary.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	
	}


