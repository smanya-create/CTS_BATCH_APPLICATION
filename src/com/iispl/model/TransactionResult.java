package com.iispl.model;

import com.iispl.enums.TransactionStatus;

public class TransactionResult {
	 private String transactionId;

	    private TransactionStatus status;

	    private String failureCode;

	    private String failureReason;

		public TransactionResult(String transactionId, TransactionStatus status, String failureCode,
				String failureReason) {
			this.transactionId = transactionId;
			this.status = status;
			this.failureCode = failureCode;
			this.failureReason = failureReason;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public TransactionStatus getStatus() {
			return status;
		}

		public void setStatus(TransactionStatus status) {
			this.status = status;
		}

		public String getFailureCode() {
			return failureCode;
		}

		public void setFailureCode(String failureCode) {
			this.failureCode = failureCode;
		}

		public String getFailureReason() {
			return failureReason;
		}

		public void setFailureReason(String failureReason) {
			this.failureReason = failureReason;
		}

		@Override
		public String toString() {
			return "TransactionResult [transactionId=" + transactionId + ", status=" + status + ", failureCode="
					+ failureCode + ", failureReason=" + failureReason + "]";
		}
		
}
