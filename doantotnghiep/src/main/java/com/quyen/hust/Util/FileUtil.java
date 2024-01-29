package com.quyen.hust.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public class FileUtil {

    public static String getFileNameWithTime(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return null;
        }
        String[] fileNames = fileName.split("\\.");
        return fileNames[0] + "_" + System.currentTimeMillis() + "." + fileNames[1];
    }

    public static String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return null;
        }
        String[] fileNames = fileName.split("\\.");
        return fileNames[fileNames.length - 1];
    }

    public static boolean isValidVideoFormat(String fileName) {
        String videoExtension = getFileExtension(fileName);
        if (!StringUtils.hasText(videoExtension)) {
            return false;
        }
        return Arrays.asList("mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "3gp", "mpg", "mpeg").contains(videoExtension);
    }

    public static boolean isValidFileFormat(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if (!StringUtils.hasText(fileExtension)) {
            return false;
        }
        return Arrays.asList("pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx").contains(fileExtension);
    }


}
