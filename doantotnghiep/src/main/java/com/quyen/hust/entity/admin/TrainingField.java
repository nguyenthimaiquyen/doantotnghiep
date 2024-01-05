package com.quyen.hust.entity.admin;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.Course;
import com.quyen.hust.entity.teacher.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "training_fields")
public class TrainingField extends BaseEntity {

    @Column(name = "field_name")
    private String fieldName;

    @Column
    private String description;

}
