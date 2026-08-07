package com.iispl.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.TransactionType;

public class TransactionRequest {
	 private String transactionId;

	    private String corporateId;

	    private String fromAccount;

	    private String toAccount;

	    private BigDecimal amount;

	    private TransactionType transactionType;

	    private LocalDate transactionDate;

		public TransactionRequest(String transactionId, String corporateId, String fromAccount, String toAccount,
				BigDecimal amount, TransactionType transactionType, LocalDate transactionDate) {
			this.transactionId = transactionId;
			this.corporateId = corporateId;
			this.fromAccount = fromAccount;
			this.toAccount = toAccount;
			this.amount = amount;
			this.transactionType = transactionType;
			this.transactionDate = transactionDate;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getCorporateId() {
			return corporateId;
		}

		public void setCorporateId(String corporateId) {
			this.corporateId = corporateId;
		}

		public String getFromAccount() {
			return fromAccount;
		}

		public void setFromAccount(String fromAccount) {
			this.fromAccount = fromAccount;
		}

		public String getToAccount() {
			return toAccount;
		}

		public void setToAccount(String toAccount) {
			this.toAccount = toAccount;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public TransactionType getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(TransactionType transactionType) {
			this.transactionType = transactionType;
		}

		public LocalDate getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(LocalDate transactionDate) {
			this.transactionDate = transactionDate;
		}

		@Override
		public String toString() {
			return "TransactionRequest [transactionId=" + transactionId + ", corporateId=" + corporateId
					+ ", fromAccount=" + fromAccount + ", toAccount=" + toAccount + ", amount=" + amount
					+ ", transactionType=" + transactionType + ", transactionDate=" + transactionDate + "]";
		}
		
}
