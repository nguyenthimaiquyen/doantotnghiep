package com.quyen.hust.service.admin;

import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.exception.TrainingFieldNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.admin.TrainingFieldRequest;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainingFieldService {
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;


    public List<TrainingFieldResponse> getAll() {
        return trainingFieldJpaRepository.findAll().stream().map(
                trainingField -> TrainingFieldResponse.builder()
                        .id(trainingField.getId())
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .imageUrl(trainingField.getImageUrl())
                        .totalCourses(trainingFieldJpaRepository.countCoursesByTrainingFieldId(trainingField.getId()))
                        .build()
        ).collect(Collectors.toList());
    }

    public TrainingFieldResponse getTrainingFieldDetails(Long id) throws TrainingFieldNotFoundException {
        return trainingFieldJpaRepository.findById(id).map(
                trainingField -> TrainingFieldResponse.builder()
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .imageUrl(trainingField.getImageUrl())
                        .build()
        ).orElseThrow(() -> new TrainingFieldNotFoundException("Training field with id" + id + " could not be found!"));
    }

    public void saveTrainingField(TrainingFieldRequest request, MultipartFile image) throws UnsupportedFormatException {
        String imageDB = null;
        if (image != null) {
            //kiểm tra định dạng image tải lên
            if (!FileUtil.isValidImageFormat(image.getOriginalFilename())) {
                throw new UnsupportedFormatException("Image formats do not support");
            }
            //lưu ảnh
            imageDB = FileUtil.getFileNameWithTime(image.getOriginalFilename());
            String imagePath = "course_data" + File.separator + "image" + File.separator + imageDB;
            try (InputStream videoInputStream = image.getInputStream()) {
                Files.copy(videoInputStream, Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TrainingField trainingField = TrainingField.builder()
                .fieldName(request.getFieldName())
                .description(request.getDescription())
                .imageUrl(imageDB)
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update a training field
            TrainingField tf = trainingFieldJpaRepository.findById(request.getId()).get();
            tf.setFieldName(request.getFieldName());
            tf.setDescription(request.getDescription());
            tf.setImageUrl(imageDB);
            trainingFieldJpaRepository.save(tf);
            return;
        }
        //create a new training field
        trainingFieldJpaRepository.save(trainingField);
    }

    public void deleteTrainingField(Long id) {
        trainingFieldJpaRepository.deleteById(id);
    }


}
