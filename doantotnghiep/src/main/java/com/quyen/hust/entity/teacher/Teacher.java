package com.quyen.hust.entity.teacher;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "teachers")
public class Teacher extends BaseEntity {

    @Column(name = "teaching_expertise")
    private String teachingExpertise;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @OneToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
