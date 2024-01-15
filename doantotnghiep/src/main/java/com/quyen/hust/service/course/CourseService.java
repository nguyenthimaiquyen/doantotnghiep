package com.quyen.hust.service.course;


import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.CourseFeeUnitResponse;
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
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<DiscountCodeResponse> getDiscountCode() {
        return discountCodeJpaRepository.findAll().stream().map(
                discountCode -> DiscountCodeResponse.builder()
                        .codeName(discountCode.getCodeName()).build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void saveCourse(CourseRequest request) {
//        Optional<User> teacher = userJpaRepository.findById(request.getTeacherID());
        List<TrainingField> trainingFields = request.getTrainingFields();
        Optional<DiscountCode> discountCode = discountCodeJpaRepository.findById(request.getDiscountID());
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .learningObjectives(request.getLearningObjectives())
                .courseFee(request.getCourseFee())
                .courseFeeUnit(request.getCourseFeeUnit())
                .difficultyLevel(request.getDifficultyLevel())
                .trainingFields(request.getTrainingFields())
//                .teacher(teacher.get())
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
            course.setTrainingFields(request.getTrainingFields());
//            course.setTeacher(request.getTeacherID());
            course.setDiscountCode(discountCode.get());
            return;
        }
        //create a new course
        course.setCourseStatus(CourseStatus.DRAFT);
        courseJpaRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseJpaRepository.deleteById(id);
    }




}
