package com.quyen.hust.service.course;


import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.CourseFeeUnitResponse;
import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.model.response.course.DifficultyLevelResponse;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.CourseStatus;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseJpaRepository courseJpaRepository;
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;
    private final DiscountCodeJpaRepository discountCodeJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

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

    public List<DiscountCodeResponse> getDiscountCode() {
        return discountCodeJpaRepository.findAll().stream().map(
                discountCode -> DiscountCodeResponse.builder()
                        .id(discountCode.getId())
                        .codeName(discountCode.getCodeName())
                        .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void saveCourse(CourseRequest request) {
        Optional<Teacher> teacher = teacherJpaRepository.findById(request.getTeacherID());
        System.out.println(request.getTeacherID());
        Optional<TrainingField> trainingField = trainingFieldJpaRepository.findById(request.getTrainingFieldID());
        Optional<DiscountCode> discountCode = discountCodeJpaRepository.findById(request.getDiscountID());
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .learningObjectives(request.getLearningObjectives())
                .courseFee(request.getCourseFee())
                .courseFeeUnit(request.getCourseFeeUnit())
                .difficultyLevel(request.getDifficultyLevel())
                .trainingField(trainingField.get())
                .teacher(teacher.get())
                .discountCode(discountCode.get())
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update course
            Course courseNeedUpdate = courseJpaRepository.findById(request.getId()).get();
            courseNeedUpdate.setTitle(request.getTitle());
            courseNeedUpdate.setDescription(request.getDescription());
            courseNeedUpdate.setLearningObjectives(request.getLearningObjectives());
            courseNeedUpdate.setCourseFee(request.getCourseFee());
            course.setCourseFeeUnit(request.getCourseFeeUnit());
            course.setDifficultyLevel(request.getDifficultyLevel());
            course.setTrainingField(trainingField.get());
            course.setTeacher(teacher.get());
            course.setDiscountCode(discountCode.get());
            return;
        }
        //create a new course
        System.out.println("vào đến đây rồi");
        course.setCourseStatus(CourseStatus.DRAFT);
        courseJpaRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseJpaRepository.deleteById(id);
    }


    public List<CourseDataResponse> getAll() {
        return courseJpaRepository.findAll().stream().map(
                course -> CourseDataResponse.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .learningObjectives(course.getLearningObjectives())
                        .courseFee(course.getCourseFee())
                        .courseFeeUnit(course.getCourseFeeUnit())
                        .discountCode(course.getDiscountCode())
                        .teacherName(course.getTeacher().getUser().getFullName())
                        .difficultyLevel(course.getDifficultyLevel())
                        .trainingField(course.getTrainingField())
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
                        .discountCode(course.getDiscountCode())
                        .teacherName(course.getTeacher().getUser().getFullName())
                        .courseStatus(course.getCourseStatus())
                        .trainingField(course.getTrainingField())
                        .build()
        ).orElseThrow(() -> new CourseNotFoundException("Course with id " + id + " could not be found!"));
    }
}
