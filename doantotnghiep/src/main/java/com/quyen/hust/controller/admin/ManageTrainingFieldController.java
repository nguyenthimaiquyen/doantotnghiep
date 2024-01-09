package com.quyen.hust.controller.admin;

import com.quyen.hust.exception.DiscountCodeNotFoundException;
import com.quyen.hust.exception.TrainingFieldNotFoundException;
import com.quyen.hust.model.request.admin.DiscountCodeRequest;
import com.quyen.hust.model.request.admin.TrainingFieldRequest;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.service.admin.TrainingFieldService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/training-fields")
public class ManageTrainingFieldController {
    private final TrainingFieldService trainingFieldService;

    @GetMapping
    public String getTrainingFieldManagementPage(Model model) {
        List<TrainingFieldResponse> trainingFields = trainingFieldService.getAll();
        model.addAttribute("trainingFields", trainingFields);
        return "admin/training-field/manage-training-field";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingFieldDetails(@PathVariable Long id) throws TrainingFieldNotFoundException {
        return ResponseEntity.ok(trainingFieldService.getTrainingFieldDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createDiscountCode(@RequestBody TrainingFieldRequest request) {
        trainingFieldService.saveTrainingField(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateTrainingField(@RequestBody TrainingFieldRequest request) {
        trainingFieldService.saveTrainingField(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrainingField(@PathVariable Long id) {
        trainingFieldService.deleteTrainingField(id);
        return ResponseEntity.ok(null);
    }


}
