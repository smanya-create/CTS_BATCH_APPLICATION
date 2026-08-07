package com.iispl.dao;

import java.sql.Connection;

import com.iispl.model.FileProcessingSummary;

public interface FileDaoProcessing {
	boolean saveSummary(Connection connection,
            FileProcessingSummary summary);
}
