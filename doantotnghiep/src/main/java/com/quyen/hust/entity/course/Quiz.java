package com.quyen.hust.entity.course;

import com.quyen.hust.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "quizzes")
public class Quiz extends BaseEntity {

    @Column
    private String title;

    @Column
    private String question;

    @Column
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;


}
