package com.iispl.model;

import java.time.LocalDateTime;

public class FileProcessingSummary {
	private String fileName;

    private int totalTransactions;

    private int successCount;

    private int failureCount;

    private LocalDateTime processedTime;

    private String status;

	public FileProcessingSummary(String fileName, int totalTransactions, int successCount, int failureCount,
			LocalDateTime processedTime, String status) {
		this.fileName = fileName;
		this.totalTransactions = totalTransactions;
		this.successCount = successCount;
		this.failureCount = failureCount;
		this.processedTime = processedTime;
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(int totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public LocalDateTime getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(LocalDateTime processedTime) {
		this.processedTime = processedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FileProcessingSummary [fileName=" + fileName + ", totalTransactions=" + totalTransactions
				+ ", successCount=" + successCount + ", failureCount=" + failureCount + ", processedTime="
				+ processedTime + ", status=" + status + "]";
	}
	
    
}
