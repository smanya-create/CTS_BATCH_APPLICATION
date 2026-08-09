package com.iispl.main;

import java.nio.file.Path;

import com.iispl.nio.FileIntakeService;

public class Fileintaketest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {

	            FileIntakeService service =
	                    new FileIntakeService();

	            service.createDirectories();

	            Path processingFile =
	                    service.processNextFile();

	            System.out.println(
	                    "Returned Path: " + processingFile);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}

}
