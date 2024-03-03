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

    public static boolean isValidImageFormat(String fileName) {
        String imageExtension = getFileExtension(fileName);
        if (!StringUtils.hasText(imageExtension)) {
            return false;
        }
        return Arrays.asList("jpg", "png", "gif", "jpeg", "svg", "webp", "ico", "tif", "bmp", "tiff").contains(imageExtension);
    }

    public static boolean isValidFileFormat(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if (!StringUtils.hasText(fileExtension)) {
            return false;
        }
        return Arrays.asList("pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx").contains(fileExtension);
    }


//    public static String getVideoDurationInSeconds(String videoPath) {
//        String formattedDuration = "";
//        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
//            grabber.start();
//            double lengthInSeconds = grabber.getLengthInTime() / 1000000.0; // chuyển đổi từ micro giây sang giây
//            int minutes = (int) (lengthInSeconds / 60);
//            int seconds = (int) (lengthInSeconds % 60);
//            formattedDuration = String.format("%d:%02d", minutes, seconds); // định dạng đầu ra phút:giây
//            grabber.stop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return formattedDuration;
//    }


}
