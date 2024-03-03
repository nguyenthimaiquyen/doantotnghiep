package com.quyen.hust.entity.user;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.course.Course;
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
@Table(name = "wishlist_items")
public class WishlistItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @OneToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
