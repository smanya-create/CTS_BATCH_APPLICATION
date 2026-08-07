-- Find account
SELECT * FROM account WHERE account_number = ?;

-- Check duplicate transaction
SELECT COUNT(*) FROM bank_transaction WHERE transaction_id = ?;

-- Debit account
UPDATE account
SET balance = balance - ?
WHERE account_number = ? AND account_status = 'ACTIVE' AND balance >= ?;

-- Credit account
UPDATE account
SET balance = balance + ?
WHERE account_number = ? AND account_status = 'ACTIVE';

-- Insert processed transaction
INSERT INTO bank_transaction
(transaction_id,batch_id,from_account,to_account,transaction_type,amount,transaction_date,transaction_status,failure_reason,source_file)
VALUES (?,?,?,?,?,?,?,?,?,?);

-- Insert file processing summary
INSERT INTO file_processing
(batch_id,file_name,total_records,successful_records,failed_records,processing_status)
VALUES (?,?,?,?,?,?);

-- Reconciliation summary
SELECT source_file,
       COUNT(*) AS total_records,
       SUM(CASE WHEN transaction_status='SUCCESS' THEN 1 ELSE 0 END) AS success_count,
       SUM(CASE WHEN transaction_status='FAILED' THEN 1 ELSE 0 END) AS failed_count
FROM bank_transaction
GROUP BY source_file;
