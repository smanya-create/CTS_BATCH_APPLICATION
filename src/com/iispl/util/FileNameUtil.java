package com.iispl.util;

import java.util.regex.Pattern;

public class FileNameUtil {
	private static final String FILE_PATTERN =
            "^TXN_[A-Z0-9]+_\\d{8}_\\d{3}\\.xml$";

    private static final Pattern pattern =
            Pattern.compile(FILE_PATTERN);

    public static boolean isValidFileName(String fileName) {
        return pattern.matcher(fileName).matches();
    }
}
