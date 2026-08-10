package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.iispl.util.FileNameUtil;

public class FileIntakeService {
	private static final Path INCOMING =
            Paths.get("data", "incoming");
    private static final Path PROCESSING =
            Paths.get("data", "processing");
    private static final Path REJECTED =
            Paths.get("data", "rejected");
    private static final Path ARCHIVE =
            Paths.get("data", "archive");
    private static final Path OUTPUT =
            Paths.get("data", "output");
//creating the physical folders
    public void createDirectories() throws IOException {

        Files.createDirectories(INCOMING);
        Files.createDirectories(PROCESSING);
        Files.createDirectories(REJECTED);
        Files.createDirectories(ARCHIVE);
        Files.createDirectories(OUTPUT);
    }
    public Path processNextFile() throws IOException {
//travering through the incoming folder
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(INCOMING)) {

            for (Path file : stream) {

                String fileName =
                        file.getFileName().toString();
                // 1. Validating the filename
                if (!FileNameUtil.isValidFileName(fileName)) {
                    System.out.println(
                            "Invalid filename: " + fileName);
                    moveToRejected(file);
                    continue;
                }
                // 2. Read the file attributes
                BasicFileAttributes attributes =
                        Files.readAttributes(
                                file,
                                BasicFileAttributes.class);
                // 3. Check is the file is a regular file
                if (!attributes.isRegularFile()) {
                    System.out.println("Not a regular file: " + fileName);
                    moveToRejected(file);
                    continue;
                }
                // 4. Check if it is a empty file
                if (attributes.size() == 0) {
                    System.out.println("Empty file: " + fileName);
                    moveToRejected(file);
                    continue;
                }

                // 5. Moving the valid file to processing
                Path destination =PROCESSING.resolve(file.getFileName());
                Files.move(file,destination,StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved to processing: "+ destination);
                return destination;
            }
        }
        return null;
    }

    private void moveToRejected(Path file)throws IOException {
//adding the invalid file to rejected folder
        Path destination = REJECTED.resolve(file.getFileName());

        Files.move(file,destination,StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File moved to rejected: "+ destination);
    }

}
