package com.quyen.hust.service.course;


import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.CourseFeeUnitResponse;
import com.quyen.hust.model.response.course.DifficultyLevelResponse;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseJpaRepository courseJpaRepository;
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;
    private final DiscountCodeJpaRepository discountCodeJpaRepository;

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



}
