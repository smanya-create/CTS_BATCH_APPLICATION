package com.iispl.service;

import java.util.List;

import com.iispl.exception.InvalidTransactionException;
import com.iispl.model.TransactionRequest;

public interface ValidationService {
	 void validateTransactions(List<TransactionRequest> requests,
             String corporateIdFromFileName)
throws InvalidTransactionException;
}
