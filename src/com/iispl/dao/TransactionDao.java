package com.iispl.dao;

import com.iispl.model.TransactionRequest;

public interface TransactionDao {
	boolean saveTransaction(TransactionRequest request);

    boolean transactionExists(String transactionId);
}
