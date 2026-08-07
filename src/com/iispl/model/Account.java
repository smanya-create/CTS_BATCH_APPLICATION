package com.iispl.model;

import java.math.BigDecimal;

import com.iispl.enums.AccountStatus;

public class Account {
	 private String accountNumber;
	    private String accountHolderName;
	    private BigDecimal balance;
	    private AccountStatus status;
		public Account(String accountNumber, String accountHolderName, BigDecimal balance, AccountStatus status) {
			this.accountNumber = accountNumber;
			this.accountHolderName = accountHolderName;
			this.balance = balance;
			this.status = status;
		}
		public String getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}
		public String getAccountHolderName() {
			return accountHolderName;
		}
		public void setAccountHolderName(String accountHolderName) {
			this.accountHolderName = accountHolderName;
		}
		public BigDecimal getBalance() {
			return balance;
		}
		public void setBalance(BigDecimal balance) {
			this.balance = balance;
		}
		public AccountStatus getStatus() {
			return status;
		}
		public void setStatus(AccountStatus status) {
			this.status = status;
		}
		@Override
		public String toString() {
			return "Account [accountNumber=" + accountNumber + ", accountHolderName=" + accountHolderName + ", balance="
					+ balance + ", status=" + status + "]";
		}
		
	    

}
