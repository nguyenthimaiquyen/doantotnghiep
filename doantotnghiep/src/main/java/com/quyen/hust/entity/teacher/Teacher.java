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

    @OneToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "teachers_training_fields",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "training_field_id"))
    private List<TrainingField> trainingFields;

}
