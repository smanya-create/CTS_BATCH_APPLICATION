package com.iispl.service;

import java.util.List;

import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public interface TransactionService {
	 List<TransactionResult> processTransactions(
	            List<TransactionRequest> requests);
}
