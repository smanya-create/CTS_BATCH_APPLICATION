package com.iispl.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDao;
import com.iispl.dao.TransactionDao;
import com.iispl.enums.AccountStatus;
import com.iispl.enums.TransactionStatus;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class TransactionServiceImpl implements TransactionService {
	 private AccountDao accountDao;
	    private TransactionDao transactionDao;


	    public TransactionServiceImpl(AccountDao accountDao,
	                                  TransactionDao transactionDao) {

	        this.accountDao = accountDao;
	        this.transactionDao = transactionDao;
	    }

	
	

	@Override
	public List<TransactionResult> processTransactions(Connection connection, List<TransactionRequest> requests) {
		// TODO Auto-generated method stub
		 List<TransactionResult> results = new ArrayList<>();
		for(TransactionRequest request : requests) {

		    TransactionResult result;


		    try {

		        // 1. Check duplicate transaction

		        boolean exists =
		                transactionDao.transactionExists(
		                        connection,
		                        request.getTransactionId()
		                );


		        if(exists) {

		            result = new TransactionResult(
		                    request.getTransactionId(),
		                    TransactionStatus.FAILED,
		                    "DUPLICATE",
		                    "Duplicate transaction ID"
		            );

		            results.add(result);
		            continue;
		        }


		        // 2. Find source account

		        Account fromAccount =
		                accountDao.findAccountByNumber(
		                        connection,
		                        request.getFromAccount()
		                );


		        // 3. Find destination account

		        Account toAccount =
		                accountDao.findAccountByNumber(
		                        connection,
		                        request.getToAccount()
		                );


		        if(fromAccount == null || toAccount == null) {

		            result = new TransactionResult(
		                    request.getTransactionId(),
		                    TransactionStatus.FAILED,
		                    "ACCOUNT_NOT_FOUND",
		                    "Account does not exist"
		            );

		            results.add(result);
		            continue;
		        }


		        // 4. Check account status

		        if(fromAccount.getStatus() != AccountStatus.ACTIVE ||
		           toAccount.getStatus() != AccountStatus.ACTIVE) {


		            result = new TransactionResult(
		                    request.getTransactionId(),
		                    TransactionStatus.FAILED,
		                    "INACTIVE_ACCOUNT",
		                    "Account is not active"
		            );


		            results.add(result);
		            continue;
		        }



		        // 5. Debit source account

		        boolean debit =
		                accountDao.debitAccount(
		                        connection,
		                        request.getFromAccount(),
		                        request.getAmount()
		                );


		        if(!debit) {

		            result = new TransactionResult(
		                    request.getTransactionId(),
		                    TransactionStatus.FAILED,
		                    "INSUFFICIENT_BALANCE",
		                    "Insufficient balance"
		            );


		            results.add(result);
		            continue;
		        }



		        // 6. Credit destination account

		        boolean credit =
		                accountDao.creditAccount(
		                        connection,
		                        request.getToAccount(),
		                        request.getAmount()
		                );


		        if(!credit) {

		            connection.rollback();


		            result = new TransactionResult(
		                    request.getTransactionId(),
		                    TransactionStatus.FAILED,
		                    "CREDIT_FAILED",
		                    "Credit operation failed"
		            );


		            results.add(result);
		            continue;
		        }



		        // 7. Save transaction

		        result = new TransactionResult(
		                request.getTransactionId(),
		                TransactionStatus.SUCCESS,
		                null,
		                null
		        );


		        transactionDao.saveTransaction(
		                connection,
		                request,
		                result
		        );


		        connection.setAutoCommit(false);


		        results.add(result);


		    }
		    catch(Exception e) {


		        try {
		            connection.rollback();
		        }
		        catch(Exception rollbackException) {
		            rollbackException.printStackTrace();
		        }


		        result = new TransactionResult(
		                request.getTransactionId(),
		                TransactionStatus.FAILED,
		                "DATABASE_ERROR",
		                e.getMessage()
		        );


		        results.add(result);
		    }

		}
		return null;
	}

}
