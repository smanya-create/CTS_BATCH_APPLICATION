package com.iispl.dao;

import java.math.BigDecimal;

import com.iispl.model.Account;

public interface AccountDao {
	 Account findAccount(String accountNumber);

	 boolean updateBalance(String accountNumber,
             BigDecimal balance);
}
