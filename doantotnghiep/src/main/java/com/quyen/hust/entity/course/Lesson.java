package com.quyen.hust.entity.course;

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
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "embedded_url")
    @Lob
    private String embeddedUrl;

    @Column(name = "video_url")
    @Lob
    private String videoUrl;

    @Column(name = "file_url")
    @Lob
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;


}
