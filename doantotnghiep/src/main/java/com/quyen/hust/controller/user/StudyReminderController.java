package com.quyen.hust.controller.user;

import com.quyen.hust.exception.StudyReminderNotFoundException;
import com.quyen.hust.model.request.user.StudyReminderRequest;
import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.service.user.StudyReminderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reminders")
@AllArgsConstructor
public class StudyReminderController {
    private final StudyReminderService studyReminderService;

    @GetMapping
    public String getReminderPage(Model model) {
        List<StudyReminderResponse> studyReminders = studyReminderService.getAll();
        model.addAttribute("studyReminders", studyReminders);
        return "myLearning/myLearning";
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getCourses() {
        return ResponseEntity.ok(studyReminderService.getCourses());
    }

    @GetMapping("/frequency")
    public ResponseEntity<?> getFrequency() {
        return ResponseEntity.ok(studyReminderService.getFrequency());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderDetails(@PathVariable Long id) throws StudyReminderNotFoundException {
        return ResponseEntity.ok(studyReminderService.getReminderDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody StudyReminderRequest request) {
        studyReminderService.saveReminder(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateReminder(@RequestBody StudyReminderRequest request) {
        studyReminderService.saveReminder(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id) {
        studyReminderService.deleteReminder(id);
        return ResponseEntity.ok(null);
    }

}
