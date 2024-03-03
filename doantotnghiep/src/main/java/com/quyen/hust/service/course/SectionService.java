package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Quiz;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.SectionRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import com.quyen.hust.model.response.course.SectionResponse;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.QuizJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SectionService {
    private final SectionJpaRepository sectionJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final QuizJpaRepository quizJpaRepository;

    public List<SectionResponse> getSections(Long courseId) {
        List<SectionResponse> sections = sectionJpaRepository.findByCourseId(courseId).stream().map(
                section -> {
                    SectionResponse sectionResponse = SectionResponse.builder()
                            .id(section.getId())
                            .title(section.getTitle())
                            .lessons(Collections.emptyList())
                            .quizzes(Collections.emptyList())
                            .build();
                    List<Lesson> lessons = lessonJpaRepository.findBySectionIdOrderByIdAsc(section.getId());
                    lessons.get(0).setPreview(true);
                    lessonJpaRepository.saveAll(lessons);
                    //cộng tổng thời gian xem video trong lesson
                    int lessonTotalMinutes = 0;
                    int lessonTotalSeconds = 0;
                    for (Lesson lesson : lessons) {
                        if (lesson.getVideoDuration() != null) {
                            String[] timeParts = lesson.getVideoDuration().split(":");
                            int minutes = Integer.parseInt(timeParts[0]);
                            int seconds = Integer.parseInt(timeParts[1]);
                            lessonTotalMinutes += minutes;
                            lessonTotalSeconds += seconds;
                        }
                    }
                    if (lessons.size() != 0) {
                        List<LessonResponse> lessonResponses = lessons.stream()
                                .map(lesson -> LessonResponse.builder()
                                        .id(lesson.getId())
                                        .title(lesson.getTitle())
                                        .embeddedUrl(lesson.getEmbeddedUrl())
                                        .videoUrl(lesson.getVideoUrl())
                                        .videoDuration(lesson.getVideoDuration())
                                        .preview(lesson.getPreview())
                                        .build()
                                )
                                .collect(Collectors.toList());
                        sectionResponse.setLessons(
                                lessonResponses
                                        .stream()
                                        .sorted(Comparator.comparingLong(LessonResponse::getId))
                                        .collect(Collectors.toList())
                        );
                    }
                    List<Quiz> quizzes = quizJpaRepository.findBySectionIdOrderByIdAsc(section.getId());
                    //cộng tổng thời gian làm các bài quiz
                    int quizTotalMinutes = 0;
                    int quizTotalSeconds = 0;
                    for (Quiz quiz : quizzes) {
                        String[] timeParts = quiz.getTimeCount().split(":");
                        int minutes = Integer.parseInt(timeParts[0]);
                        int seconds = Integer.parseInt(timeParts[1]);
                        quizTotalMinutes += minutes;
                        quizTotalSeconds += seconds;
                    }
                    if (quizzes.size() != 0) {
                        List<QuizResponse> quizResponses = quizzes.stream()
                                .map(quiz -> QuizResponse.builder()
                                        .id(quiz.getId())
                                        .title(quiz.getTitle())
                                        .timeCount(quiz.getTimeCount())
                                        .build()
                                ).collect(Collectors.toList());
                        sectionResponse.setQuizzes(
                                quizResponses
                                        .stream()
                                        .sorted(Comparator.comparingLong(QuizResponse::getId))
                                        .collect(Collectors.toList())
                        );
                    }
                    int overflowSeconds = (quizTotalSeconds + lessonTotalSeconds) / 60;
                    int sectionTotalMinutes = quizTotalMinutes + lessonTotalMinutes + overflowSeconds;
                    int sectionTotalSeconds = (quizTotalSeconds + lessonTotalSeconds) % 60;
                    String totalTimeCount = String.format("%d:%02d", sectionTotalMinutes, sectionTotalSeconds);
                    sectionResponse.setTotalTime(totalTimeCount);
                    sectionResponse.setTotalLessons(lessons.size());
                    sectionResponse.setTotalQuizzes(quizzes.size());
                    section.setTotalTime(totalTimeCount);
                    section.setTotalLessons(lessons.size());
                    section.setTotalQuizzes(quizzes.size());
                    sectionJpaRepository.save(section);
                    return sectionResponse;
                }).collect(Collectors.toList());
        return sections;
    }

    @Transactional
    public void saveSection(SectionRequest request) {
        Optional<Course> course = courseJpaRepository.findById(request.getCourseId());
        Section section = Section.builder()
                .title(request.getTitle())
                .course(course.get())
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update section
            Section sectionNeedUpdate = sectionJpaRepository.findById(request.getId()).get();
            sectionNeedUpdate.setTitle(request.getTitle());
            return;
        }
        //create section
        sectionJpaRepository.save(section);
    }

    public void deleteSection(Long id) throws SectionNotFoundException {
        Optional<Section> sectionOptional = sectionJpaRepository.findById(id);
        if (!sectionOptional.isPresent()) {
            throw new SectionNotFoundException("Section with id " + id + " could not be found!");
        }
        Section section = sectionOptional.get();
        List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
        List<Quiz> quizzes = quizJpaRepository.findBySectionId(section.getId());
        quizJpaRepository.deleteAll(quizzes);
        lessonJpaRepository.deleteAll(lessons);
        sectionJpaRepository.deleteById(id);
    }


    public SectionResponse getSectionDetails(Long id) throws SectionNotFoundException {
        return sectionJpaRepository.findById(id).map(
                section -> SectionResponse.builder()
                        .id(section.getId())
                        .title(section.getTitle())
                        .build()
        ).orElseThrow(() -> new SectionNotFoundException("Section with id " + id + " could not be found!"));
    }


}
