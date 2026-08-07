package com.iispl.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import com.iispl.model.Account;

public interface AccountDao {
	Account findAccountByNumber(Connection connection, String accountNumber);

    boolean debitAccount(Connection connection,
                         String accountNumber,
                         BigDecimal amount);

    boolean creditAccount(Connection connection,
                          String accountNumber,
                          BigDecimal amount);
}
