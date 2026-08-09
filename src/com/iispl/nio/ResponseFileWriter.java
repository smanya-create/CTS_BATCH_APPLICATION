package com.iispl.nio;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.iispl.exception.ResponseGenerationException;
import com.iispl.model.TransactionResult;
import com.iispl.service.TransactionServiceImpl;
public class ResponseFileWriter {
	List<TransactionResult> transactionResultList=TransactionServiceImpl.getTransactionResultList(); 
	

    public Path writeResponse( Path outputDirectory,String originalFileName,List<TransactionResult> results) throws ResponseGenerationException {

        try {
            Files.createDirectories(outputDirectory);

            String responseFileName = "RESP_" + originalFileName;

            Path responsePath =
                    outputDirectory.resolve(responseFileName);

            StringBuilder xml = new StringBuilder();

            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<Response>\n");

            for (TransactionResult result : results) {

                xml.append("    <Transaction>\n");

                xml.append("        <transactionId>")
                   .append(result.getTransactionId())
                   .append("</transactionId>\n");

                xml.append("        <status>")
                   .append(result.getStatus())
                   .append("</status>\n");

                xml.append("        <failureCode>")
                   .append(result.getFailureCode() == null
                           ? ""
                           : result.getFailureCode())
                   .append("</failureCode>\n");

                xml.append("        <failureReason>")
                   .append(result.getFailureReason() == null
                           ? ""
                           : result.getFailureReason())
                   .append("</failureReason>\n");

                xml.append("    </Transaction>\n");
            }

            xml.append("</Response>\n");

            byte[] data =
                    xml.toString().getBytes(StandardCharsets.UTF_8);

            ByteBuffer buffer = ByteBuffer.wrap(data);

            try (FileChannel channel = FileChannel.open(
                    responsePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }

            return responsePath;

        } catch (IOException e) {
            throw new ResponseGenerationException("Failed to generate response file");
        }
    }
    
    
    
    public Path writeSummary(
            Path outputDirectory,
            String originalFileName,
            List<TransactionResult> results)
            throws ResponseGenerationException {

        int total = results.size();

        int successful = 0;
        int failed = 0;

        for (TransactionResult result : results) {

            if ("SUCCESS".equals(result.getStatus())) {

                successful++;

            } else {

                failed++;
            }
        }

        try {

            Files.createDirectories(outputDirectory);

            String summaryFileName =
                    "SUMMARY_"
                    + originalFileName.replace(".xml", ".txt");

            Path summaryPath =
                    outputDirectory.resolve(summaryFileName);

            String summary =
                    "File Name: " + originalFileName + "\n" +
                    "Total Transactions: " + total + "\n" +
                    "Successful Transactions: " + successful + "\n" +
                    "Failed Transactions: " + failed + "\n" +
                    "Processing Status: COMPLETED\n";

            Files.writeString(
                    summaryPath,
                    summary,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return summaryPath;

        } catch (IOException e) {

            throw new ResponseGenerationException(
                    "Failed to generate summary file");
        }
    }
    
    
    public void displayFileAttributes(Path responsePath) throws ResponseGenerationException {

        try {

            long size = Files.size(responsePath);

            System.out.println(
                    "Response File Size: " + size + " bytes");

            System.out.println(
                    "Last Modified: " +
                    Files.getLastModifiedTime(responsePath));

        } catch (IOException e) {

            throw new ResponseGenerationException("Unable to read response file attributes");
        }
    }
}
