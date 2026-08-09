	package com.iispl.nio;

	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.StandardCopyOption;

	import com.iispl.exception.ArchivalException;

	public class ArchiveService {

	    public Path moveToArchive( Path processingFile,Path archiveDirectory) throws ArchivalException {

	        try {
	            Files.createDirectories(archiveDirectory);

	            Path destination =
	                    archiveDirectory.resolve(
	                            processingFile.getFileName());

	            return Files.move(
	                    processingFile,
	                    destination,
	                    StandardCopyOption.REPLACE_EXISTING);

	        } catch (IOException e) {

	            throw new ArchivalException(
	                    "Failed to move file to archive");
	        }
	    }


	    public Path moveToRejected(Path processingFile,Path rejectedDirectory) throws ArchivalException {

	        try {

	            Files.createDirectories(rejectedDirectory);

	            Path destination =
	                    rejectedDirectory.resolve(
	                            processingFile.getFileName());

	            return Files.move(
	                    processingFile,
	                    destination,
	                    StandardCopyOption.REPLACE_EXISTING);

	        } catch (IOException e) {

	            throw new ArchivalException(
	                    "Failed to move file to rejected folder");
	        }
	    }
	}


