CREATE TABLE account (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) NOT NULL,
    account_status VARCHAR(20) NOT NULL
);

CREATE TABLE bank_transaction (
    transaction_id VARCHAR(30) PRIMARY KEY,
    batch_id VARCHAR(40) NOT NULL,
    from_account VARCHAR(20),
    to_account VARCHAR(20),
    transaction_type VARCHAR(30),
    amount DECIMAL(15,2),
    transaction_date DATE,
    transaction_status VARCHAR(20),
    failure_reason VARCHAR(255),
    source_file VARCHAR(150),
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE file_processing (
    batch_id VARCHAR(40) PRIMARY KEY,
    file_name VARCHAR(150) UNIQUE NOT NULL,
    total_records INT DEFAULT 0,
    successful_records INT DEFAULT 0,
    failed_records INT DEFAULT 0,
    processing_status VARCHAR(30),
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
