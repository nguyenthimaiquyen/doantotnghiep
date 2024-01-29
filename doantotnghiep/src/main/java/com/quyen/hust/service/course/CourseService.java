package com.quyen.hust.service.course;


import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.request.course.CourseStatusRequest;
import com.quyen.hust.model.request.SearchRequest;
import com.quyen.hust.model.response.admin.DiscountCodeDataResponse;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.CourseFeeUnitResponse;
import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.model.response.course.DifficultyLevelResponse;
import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.CourseRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.statics.CourseStatus;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseJpaRepository courseJpaRepository;
    private final CourseRepository courseRepository;
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;
    private final DiscountCodeJpaRepository discountCodeJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final SectionJpaRepository sectionJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;

    public List<CourseFeeUnitResponse> getCourseFeeUnit() {
        return List.of(
                CourseFeeUnitResponse.builder().code(Unit.USD.getCode()).name(Unit.USD.getName()).build(),
                CourseFeeUnitResponse.builder().code(Unit.VND.getCode()).name(Unit.VND.getName()).build()
        );
    }

    public List<DifficultyLevelResponse> getDifficultyLevel() {
        return List.of(
                DifficultyLevelResponse.builder().code(DifficultyLevel.BEGINNER.getCode()).name(DifficultyLevel.BEGINNER.getName()).build(),
                DifficultyLevelResponse.builder().code(DifficultyLevel.INTERMEDIATE.getCode()).name(DifficultyLevel.INTERMEDIATE.getName()).build(),
                DifficultyLevelResponse.builder().code(DifficultyLevel.ADVANCED.getCode()).name(DifficultyLevel.ADVANCED.getName()).build()
        );
    }


    public List<TrainingFieldResponse> getTrainingField() {
        return trainingFieldJpaRepository.findAll().stream().map(
                trainingField -> TrainingFieldResponse.builder()
                        .id(trainingField.getId())
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<DiscountCodeDataResponse> getDiscountCode() {
        return discountCodeJpaRepository.findAll().stream().map(
                discountCode -> DiscountCodeDataResponse.builder()
                        .id(discountCode.getId())
                        .codeName(discountCode.getCodeName())
                        .build()
        ).collect(Collectors.toList());
    }

    public void saveCourse(CourseRequest request) {
        Teacher teacher;
        if (request.getTeacherID() != null) {
            teacher = teacherJpaRepository.findById(request.getTeacherID()).get();
        } else {
            Optional<Long> id = SecurityUtils.getCurrentUserLoginId();
            teacher = teacherJpaRepository.findByUserId(id.get());
        }
        Optional<DiscountCode> discountCode = Optional.empty();
        if (request.getDiscountID() != null) {
            discountCode = discountCodeJpaRepository.findById(request.getDiscountID());
        }
        Optional<TrainingField> trainingField = trainingFieldJpaRepository.findById(request.getTrainingFieldID());
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .learningObjectives(request.getLearningObjectives())
                .courseFee(request.getCourseFee())
                .courseFeeUnit(request.getCourseFeeUnit())
                .difficultyLevel(request.getDifficultyLevel())
                .trainingField(trainingField.orElse(null))
                .teacher(teacher)
                .discountCode(discountCode.orElse(null))
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update course
            Course courseNeedUpdate = courseJpaRepository.findById(request.getId()).orElse(null);
            if (courseNeedUpdate != null) {
                courseNeedUpdate.setTitle(request.getTitle());
                courseNeedUpdate.setDescription(request.getDescription());
                courseNeedUpdate.setLearningObjectives(request.getLearningObjectives());
                courseNeedUpdate.setCourseFee(request.getCourseFee());
                courseNeedUpdate.setCourseFeeUnit(request.getCourseFeeUnit());
                courseNeedUpdate.setDifficultyLevel(request.getDifficultyLevel());
                courseNeedUpdate.setTrainingField(trainingField.orElse(null));
                courseNeedUpdate.setTeacher(teacher);
                courseNeedUpdate.setDiscountCode(discountCode.orElse(null));
                courseJpaRepository.save(courseNeedUpdate);
            }
            return;
        }
        //create a new course
        course.setCourseStatus(CourseStatus.DRAFT);
        courseJpaRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Optional<Course> courseOptional = courseJpaRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Section> sections = sectionJpaRepository.findByCourseId(course.getId());
            for (Section section : sections) {
                List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
                lessonJpaRepository.deleteAll(lessons);
            }
            sectionJpaRepository.deleteAll(sections);
        }
        courseJpaRepository.deleteById(id);
    }


    public List<CourseDataResponse> getAll() {
        //dùng hàm search,
        return courseJpaRepository.findAll().stream().map(
                course -> CourseDataResponse.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .learningObjectives(course.getLearningObjectives())
                        .courseFee(course.getCourseFee())
                        .courseFeeUnit(course.getCourseFeeUnit())
                        .teacher(TeacherResponse.builder()
                                .id(course.getTeacher().getId())
                                .user(UserDataResponse.builder()
                                        .id(course.getTeacher().getUser().getId())
                                        .fullName(course.getTeacher().getUser().getFullName())
                                        .email(course.getTeacher().getUser().getEmail())
                                        .userStatus(course.getTeacher().getUser().getUserStatus())
                                        .build())
                                .yearsOfExperience(course.getTeacher().getYearsOfExperience())
                                .teachingExpertise(course.getTeacher().getTeachingExpertise())
                                .build())
                        .difficultyLevel(course.getDifficultyLevel())
                        .trainingField(TrainingFieldResponse.builder()
                                .id(course.getTrainingField().getId())
                                .fieldName(course.getTrainingField().getFieldName())
                                .build())
                        .courseStatus(course.getCourseStatus())
                        .build()
        ).collect(Collectors.toList());
    }

    public CourseDataResponse getCourseDetails(Long id) throws CourseNotFoundException {
        return courseJpaRepository.findById(id).map(
                course -> CourseDataResponse.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .learningObjectives(course.getLearningObjectives())
                        .courseFee(course.getCourseFee())
                        .courseFeeUnit(course.getCourseFeeUnit())
                        .difficultyLevel(course.getDifficultyLevel())
                        .courseStatus(course.getCourseStatus())
                        .build()
        ).orElseThrow(() -> new CourseNotFoundException("Course with id " + id + " could not be found!"));
    }

    public void changeCourseStatus(Long courseId, CourseStatusRequest status) throws CourseNotFoundException {
        Course course = courseJpaRepository.findById(courseId).orElse(null);
        if (ObjectUtils.isEmpty(course)) {
            throw new CourseNotFoundException("Course with id " + courseId + " could not be found!");
        }
        course.setCourseStatus(status.getCourseStatus());
        courseJpaRepository.save(course);
    }


    public CourseResponse searchCourse(SearchRequest request) {
        List<CourseDataResponse> data = courseRepository.searchCourse(request);
        Long totalElement = 0L;
        if (!CollectionUtils.isEmpty(data)) {
            totalElement = data.get(0).getTotalRecord();
        }
        double totalPageTemp = (double) totalElement / request.getPageSize();
        if (totalPageTemp > totalElement / request.getPageSize()) {
            totalPageTemp++;
        }
        return CourseResponse.builder()
                .courses(data)
                .totalElement(totalElement)
                .totalPage(Double.valueOf(totalPageTemp).intValue())
                .currentPage(request.getCurrentPage())
                .pageSize(request.getPageSize())
                .build();
    }


}
