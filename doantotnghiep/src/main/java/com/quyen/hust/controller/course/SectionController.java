package com.quyen.hust.controller.course;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.SectionRequest;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.course.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/sections")
public class SectionController {
    private final SectionService sectionService;
    private final CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSectionDetails(@PathVariable Long id) throws SectionNotFoundException {
        return ResponseEntity.ok(sectionService.getSectionDetails(id));
    }

    @GetMapping
    public ResponseEntity<?> getSections(@PathVariable Long id) throws SectionNotFoundException, CourseNotFoundException {
        return ResponseEntity.ok(sectionService.getSections(id));
    }

    @PostMapping
    public ResponseEntity<?> createSection(@RequestBody @Valid SectionRequest request) {
        sectionService.saveSection(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateSection(@RequestBody @Valid SectionRequest request) {
        sectionService.saveSection(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok(null);
    }

}
