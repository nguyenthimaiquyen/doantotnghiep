package com.quyen.hust.entity.course;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.CourseStatus;
import com.quyen.hust.statics.Unit;
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
@Table(name = "courses")
public class Course extends BaseEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "learning_objectives")
    private String learningObjectives;

    @Column(name = "course_fee")
    private Double courseFee;

    @Column(name = "course_fee_unit", length = 20)
    @Enumerated(EnumType.STRING)
    private Unit courseFeeUnit;

    @Column
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Column(name = "total_lessons")
    private Integer totalLessons;

    @Column(name = "total_video_duration")
    private Long totalVideoDuration;

    @Column(name = "total_test")
    private Long totalTest;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "discount_id", nullable = false)
    private DiscountCode discountCode;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "courses_training_fields",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "training_field_id"))
    private List<TrainingField> trainingFields;






}
