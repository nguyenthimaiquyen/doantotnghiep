package com.quyen.hust.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quyen.hust.exception.TrainingFieldNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.admin.TrainingFieldRequest;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.service.admin.TrainingFieldService;
import com.quyen.hust.util.LongTypeAdapter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/training-fields")
public class TrainingFieldController {
    private final TrainingFieldService trainingFieldService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingFieldDetails(@PathVariable Long id) throws TrainingFieldNotFoundException {
        return ResponseEntity.ok(trainingFieldService.getTrainingFieldDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createDiscountCode(@RequestPart("trainingFieldRequest") @Valid String trainingFieldRequest,
                                                @RequestPart(value = "image", required = false) MultipartFile image) throws UnsupportedFormatException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter()).create();
        TrainingFieldRequest request = gson.fromJson(trainingFieldRequest, TrainingFieldRequest.class);
        trainingFieldService.saveTrainingField(request, image);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateTrainingField(@RequestPart("trainingFieldRequest") @Valid String trainingFieldRequest,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) throws UnsupportedFormatException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter()).create();
        TrainingFieldRequest request = gson.fromJson(trainingFieldRequest, TrainingFieldRequest.class);
        trainingFieldService.saveTrainingField(request, image);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrainingField(@PathVariable Long id) {
        trainingFieldService.deleteTrainingField(id);
        return ResponseEntity.ok(null);
    }


}
