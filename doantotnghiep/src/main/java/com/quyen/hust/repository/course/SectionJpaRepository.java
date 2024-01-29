package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionJpaRepository extends JpaRepository<Section, Long> {

    List<Section> findByCourseId(Long id);



}
