package com.iispl.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDao;
import com.iispl.dao.TransactionDao;
import com.iispl.daoimple.AccountDAOImpl;
import com.iispl.daoimple.TransactionDAOImpl;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.FileIntakeService;
import com.iispl.nio.ResponseFileWriter;
import com.iispl.nio.XmlReader;
import com.iispl.service.TransactionServiceImpl;
import com.iispl.util.DBUtil;

public class CTSBatchApplication {

    public static void main(String[] args) {

        Connection connection = null;

        try {

            System.out.println(
                    "============================================================");
            System.out.println(
                    "        CTS BULK TRANSACTION PROCESSING SYSTEM");
            System.out.println(
                    "============================================================");


            // =====================================================
            // 1. MONITOR INCOMING FOLDER
            // =====================================================

            System.out.println("\n1. MONITOR INCOMING FOLDER");
            System.out.println(
                    "------------------------------------------------------------");

            FileIntakeService fileIntakeService =
                    new FileIntakeService();

            fileIntakeService.createDirectories();

            Path processingFile =
                    fileIntakeService.processNextFile();

            if (processingFile == null) {

                System.out.println(
                        "No valid transaction file found.");

                return;
            }

            System.out.println(
                    "Processing file: " + processingFile);


            // =====================================================
            // 2. XML PROCESSING
            // =====================================================

            System.out.println("\n2. XML PROCESSING");
            System.out.println(
                    "------------------------------------------------------------");

            XmlReader xmlReader =
                    new XmlReader();

            xmlReader.readXml(processingFile);

            List<TransactionRequest> requests =
                    xmlReader.getTransactionList();

            List<TransactionResult> xmlFailures =
                    xmlReader.getFailureTransactionList();

            System.out.println(
                    "Total transactions found : "
                    + (requests.size() + xmlFailures.size()));

            System.out.println(
                    "Valid transactions       : "
                    + requests.size());

            System.out.println(
                    "XML validation failures  : "
                    + xmlFailures.size());


            // =====================================================
            // 3. DATABASE UPDATE
            // =====================================================

            System.out.println("\n3. DATABASE UPDATE");
            System.out.println(
                    "------------------------------------------------------------");

            connection = DBUtil.getConnection();

            connection.setAutoCommit(false);

            System.out.println(
                    "Database connection established.");

            AccountDao accountDao =
                    new AccountDAOImpl();

            TransactionDao transactionDao =
                    new TransactionDAOImpl();

            TransactionServiceImpl transactionService =
                    new TransactionServiceImpl(
                            accountDao,
                            transactionDao);

            List<TransactionResult> transactionResults =
                    transactionService.processTransactions(
                            connection,
                            requests);


            System.out.println("\nTransaction Results:");

            for (TransactionResult result :
                    transactionResults) {

                System.out.println(
                        result.getTransactionId()
                        + " | "
                        + result.getStatus()
                        + " | "
                        + result.getFailureCode()
                        + " | "
                        + result.getFailureReason());
            }


            // =====================================================
            // COMBINE ALL RESULTS
            // =====================================================

            List<TransactionResult> allResults =
                    new ArrayList<>();

            // Member 2 failures
            allResults.addAll(xmlFailures);

            // Member 3 results
            allResults.addAll(transactionResults);


            // Commit database changes
            connection.commit();

            System.out.println(
                    "\nDatabase update completed successfully.");


            // =====================================================
            // 4. RESPONSE FILE GENERATION
            // =====================================================

            System.out.println(
                    "\n4. RESPONSE FILE GENERATION");

            System.out.println(
                    "------------------------------------------------------------");

            ResponseFileWriter responseWriter =
                    new ResponseFileWriter();

            Path outputDirectory =
                    Paths.get("data", "output");

            String originalFileName =
                    processingFile.getFileName().toString();

            Path responseFile =
                    responseWriter.writeResponse(
                            outputDirectory,
                            originalFileName,
                            allResults);

            System.out.println(
                    "Response file created: "
                            + responseFile);

            responseWriter.displayFileAttributes(
                    responseFile);


            // =====================================================
            // 5. ARCHIVE
            // =====================================================

            System.out.println("\n5. ARCHIVE");
            System.out.println(
                    "------------------------------------------------------------");

            Path archiveDirectory =
                    Paths.get("data", "archive");

            Files.createDirectories(archiveDirectory);

            Path archiveFile =
                    archiveDirectory.resolve(
                            processingFile.getFileName());

            Files.move(
                    processingFile,
                    archiveFile,
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println(
                    "File archived successfully: "
                            + archiveFile);


            // =====================================================
            // 6. SUMMARY
            // =====================================================

            System.out.println("\n6. SUMMARY");
            System.out.println(
                    "------------------------------------------------------------");

            int total =
                    allResults.size();

            int successful = 0;
            int failed = 0;

            for (TransactionResult result :
                    allResults) {

                if ("SUCCESS".equals(
                        result.getStatus())) {

                    successful++;

                } else {

                    failed++;
                }
            }

            System.out.println(
                    "File Name              : "
                            + originalFileName);

            System.out.println(
                    "Total Transactions     : "
                            + total);

            System.out.println(
                    "Successful Transactions: "
                            + successful);

            System.out.println(
                    "Failed Transactions    : "
                            + failed);

            System.out.println(
                    "Processing Status      : COMPLETED");


            // =====================================================
            // SUMMARY FILE
            // =====================================================

            Path summaryFile =
                    responseWriter.writeSummary(
                            outputDirectory,
                            originalFileName,
                            allResults);

            System.out.println(
                    "Summary file created: "
                            + summaryFile);


            // =====================================================
            // CLOSE CONNECTION
            // =====================================================

            connection.close();


            System.out.println(
                    "\n============================================================");

            System.out.println(
                    "        CTS PROCESSING COMPLETED SUCCESSFULLY");

            System.out.println(
                    "============================================================");


        } catch (Exception e) {

            System.err.println(
                    "\nCTS processing failed.");

            e.printStackTrace();

            try {

                if (connection != null) {
                    connection.rollback();
                    connection.close();
                }

            } catch (Exception rollbackException) {

                rollbackException.printStackTrace();
            }
        }
    }
}