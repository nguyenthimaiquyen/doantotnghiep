package com.quyen.hust.model.response.teacher;

import com.quyen.hust.model.response.user.UserDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherResponse {

    private Long id;

    private String teachingExpertise;

    private Integer yearsOfExperience;

    private UserDataResponse user;


}
