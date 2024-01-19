package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.SectionRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.SectionResponse;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SectionService {
    private final SectionJpaRepository sectionJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;

    @Transactional
    public List<SectionResponse> getSections(Long courseId) {
        return sectionJpaRepository.findByCourseId(courseId).stream().map(
                section -> {
                    SectionResponse sectionResponse = SectionResponse.builder()
                            .id(section.getId())
                            .title(section.getTitle())
                            .lessons(new ArrayList<>())
                            .build();
                    if (section.getLessons() != null) {
                        sectionResponse.setLessons(section.getLessons().stream().map(
                                lesson -> LessonResponse.builder()
                                        .id(lesson.getId())
                                        .title(lesson.getTitle())
                                        .build()
                        ).collect(Collectors.toList()));
                    }
                    return sectionResponse;
                }).collect(Collectors.toList());
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

    public void deleteSection(Long id) {
        Optional<Section> sectionOptional = sectionJpaRepository.findById(id);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
            lessons.forEach(lesson -> lessonJpaRepository.deleteById(lesson.getId()));
        }
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
