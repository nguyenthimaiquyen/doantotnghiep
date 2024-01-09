package com.quyen.hust.entity.admin;

import com.quyen.hust.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
