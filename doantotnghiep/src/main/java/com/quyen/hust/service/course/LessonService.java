package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.SectionResponse;
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


    public LessonResponse getLessonDetails(Long lessonId) throws LessonNotFoundException {
        Section section = lessonJpaRepository.findById(lessonId).get().getSection();
        SectionResponse sectionResponse = SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .build();
        return lessonJpaRepository.findById(lessonId).map(
                lesson -> LessonResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .embeddedUrl(lesson.getEmbeddedUrl())
                        .fileUrl(lesson.getFileUrl())
                        .videoUrl(lesson.getVideoUrl())
                        .section(sectionResponse)
                        .build()
        ).orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!"));
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
        if (file != null) {
            String filePath = "course_data" + File.separator + "file" + File.separator + file.getOriginalFilename();
            try {
                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (video != null) {
            String videoPath = "course_data" + File.separator + "video" + File.separator + video.getOriginalFilename();
            try {
                Files.copy(video.getInputStream(), Paths.get(videoPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .section(sectionOptional.get())
                .embeddedUrl(request.getEmbeddedUrl())
                .build();
        if (file != null) {
            lesson.setFileUrl(file.getOriginalFilename());
        }
        if (video != null) {
            lesson.setVideoUrl(video.getOriginalFilename());
        }

        if (!ObjectUtils.isEmpty(request.getId())) {
            //update lesson
            Lesson lessonNeedUpdate = lessonJpaRepository.findById(request.getId()).get();
            lessonNeedUpdate.setTitle(request.getTitle());
            lessonNeedUpdate.setContent(request.getContent());
            lessonNeedUpdate.setEmbeddedUrl(request.getEmbeddedUrl());
            if (file != null) {
                lessonNeedUpdate.setFileUrl(file.getOriginalFilename());
            }
            if (video != null) {
                lessonNeedUpdate.setVideoUrl(video.getOriginalFilename());
            }
            lessonJpaRepository.save(lessonNeedUpdate);
            return;
        }
        //create lesson
        lessonJpaRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            Section section = lessonOptional.get().getSection();
            List<Lesson> lessons = section.getLessons();
            lessons.removeIf(lesson -> lesson.getId().equals(lessonId));
            sectionJpaRepository.save(section);
        }
    }
}
