package com.quyen.hust.controller.admin;

import com.quyen.hust.util.FileUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @GetMapping("/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) throws IOException {
        if (!StringUtils.hasText(fileName)) {
            return ResponseEntity.badRequest().body("File name is empty");
        }
        File file = null;
        if (FileUtil.isValidImageFormat(fileName)) {
            file = new File("course_data/image/" + fileName);
        } else if (FileUtil.isValidVideoFormat(fileName)) {
            file = new File("course_data/video/" + fileName);
        } else if (FileUtil.isValidFileFormat(fileName)) {
            file = new File("course_data/file/" + fileName);
        } else {
            return ResponseEntity.badRequest().body("Invalid file format");
        }
        HttpHeaders headers = new HttpHeaders();
        List<String> customHeaders = new ArrayList<>();
        customHeaders.add(HttpHeaders.CONTENT_DISPOSITION);
        customHeaders.add("Content-Response");
        headers.setAccessControlExposeHeaders(customHeaders);
        headers.set("Content-disposition", "attachment;filename=" + file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        byte[] data = Files.readAllBytes(file.toPath());
        if (ObjectUtils.isEmpty(data)) {
            return ResponseEntity.noContent().build();
        }
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }



}
