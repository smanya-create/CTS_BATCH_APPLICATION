package com.iispl.util;



import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {
	private static ComboPooledDataSource dataSource;

    static {

        dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass("org.postgresql.Driver");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/cts_db");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");

        // Pool Configuration
        dataSource.setInitialPoolSize(5);
        dataSource.setMinPoolSize(2);
        dataSource.setMaxPoolSize(10);
        dataSource.setAcquireIncrement(2);

    }

    public static Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }

    }
	


