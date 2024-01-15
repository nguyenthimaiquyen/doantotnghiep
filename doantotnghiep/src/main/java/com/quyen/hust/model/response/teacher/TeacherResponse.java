package com.quyen.hust.model.response.teacher;

import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherResponse {

    private Long id;

    private String teachingExpertise;

    private Integer yearsOfExperience;

    private User user;

    private List<TrainingField> trainingFields;

}
