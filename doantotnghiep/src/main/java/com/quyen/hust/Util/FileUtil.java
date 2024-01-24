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

    public static boolean isValidVideoFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            String[] parts = contentType.split("/");
            if (parts.length == 2) {
                String extension = parts[1].toLowerCase();
                return Arrays.asList("mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "3gp", "mpg", "mpeg").contains(extension);
            }
        }
        return false;
    }

    public static boolean isValidFileFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            // Xác định extension từ contentType
            String[] parts = contentType.split("/");
            if (parts.length == 2) {
                String extension = parts[1].toLowerCase();
                // Kiểm tra xem extension có thuộc danh sách cho phép không
                return Arrays.asList("pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx").contains(extension);
            }
        }
        return false;
    }


}
