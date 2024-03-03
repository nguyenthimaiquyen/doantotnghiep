package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrainingFieldJpaRepository extends JpaRepository<TrainingField, Long> {

    TrainingField findByFieldName(String fieldName);

    @Query("select count(c) from Course c join c.trainingField tf where tf.id = :trainingFieldId")
    Integer countCoursesByTrainingFieldId(Long trainingFieldId);

}
