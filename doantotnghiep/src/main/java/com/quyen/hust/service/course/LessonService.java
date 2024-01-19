package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {
    private final SectionJpaRepository sectionJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;


    public LessonResponse getLessonDetails(Long id) throws LessonNotFoundException {
        return lessonJpaRepository.findById(id).map(
                lesson -> LessonResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .embeddedUrl(lesson.getEmbeddedUrl())
                        .fileUrl(lesson.getFileUrl())
                        .videoUrl(lesson.getVideoUrl())
                        .build()
        ).orElseThrow(() -> new LessonNotFoundException("Lesson with id " + id + " could not be found!"));
    }

    public List<LessonResponse> getLessons(Long sectionId) {
        return lessonJpaRepository.findBySectionId(sectionId).stream().map(
                lesson -> LessonResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .embeddedUrl(lesson.getEmbeddedUrl())
                        .videoUrl(lesson.getVideoUrl())
                        .fileUrl(lesson.getFileUrl())
                        .build()
        ).collect(Collectors.toList());
    }

    public void saveLesson(LessonRequest request, MultipartFile file, MultipartFile video) {
        //lưu file, video
        String filePath = "course_data" + File.separator + "file" + File.separator + file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String videoPath = "course_data" + File.separator + "video" + File.separator + video.getOriginalFilename();
        try {
            Files.copy(video.getInputStream(), Paths.get(videoPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .section(sectionOptional.get())
                .embeddedUrl(request.getEmbeddedUrl())
                .videoUrl(video.getOriginalFilename())
                .fileUrl(file.getOriginalFilename())
                .build();

        if (!ObjectUtils.isEmpty(request.getId())) {
            //update lesson
            Lesson lessonNeedUpdate = lessonJpaRepository.findById(request.getId()).get();
            lessonNeedUpdate.setTitle(request.getTitle());
            lessonNeedUpdate.setContent(request.getContent());
            lessonNeedUpdate.setEmbeddedUrl(request.getEmbeddedUrl());
            lessonNeedUpdate.setFileUrl(file.getOriginalFilename());
            lessonNeedUpdate.setVideoUrl(video.getOriginalFilename());
            lessonJpaRepository.save(lessonNeedUpdate);
            return;
        }
        //create lesson
        lessonJpaRepository.save(lesson);
    }

    public void deleteLesson(Long id) {
        lessonJpaRepository.deleteById(id);
    }
}
