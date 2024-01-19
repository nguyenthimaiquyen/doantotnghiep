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

    public void saveLesson(LessonRequest request) {
        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .section(sectionOptional.get())
                .embeddedUrl(request.getEmbeddedUrl())
                .videoUrl(request.getVideoUrl())
                .fileUrl(request.getFileUrl())
                .build();

        if (!ObjectUtils.isEmpty(request.getId())) {
            //update lesson
            Lesson lessonNeedUpdate = lessonJpaRepository.findById(request.getId()).get();
            lessonNeedUpdate.setTitle(request.getTitle());
            lessonNeedUpdate.setContent(request.getContent());
            lessonNeedUpdate.setEmbeddedUrl(request.getEmbeddedUrl());
            lessonNeedUpdate.setFileUrl(request.getFileUrl());
            lessonNeedUpdate.setVideoUrl(request.getVideoUrl());
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
