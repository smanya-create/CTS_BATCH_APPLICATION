package com.iispl.dao;

import java.sql.Connection;

import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public interface TransactionDao {
	  boolean transactionExists(Connection connection,
              String transactionId);

boolean saveTransaction(Connection connection,
            TransactionRequest request,
            TransactionResult result);
}
