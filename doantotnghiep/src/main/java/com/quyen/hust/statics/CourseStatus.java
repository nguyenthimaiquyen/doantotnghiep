package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseStatus {

    DRAFT("DRAFT", "Phác thảo"),
    WAITING_FOR_REVIEW( "WAITING_FOR_REVIEW", "Chờ xem xét"),
    APPROVED("APPROVED", "Chấp thuận"),
    REJECTED("REJECTED", "Bị từ chối"),
    PUBLISHED("PUBLISHED", "Đã công bố"),
    UNPUBLISHED("UNPUBLISHED", "Đã hủy công bố"),
    ARCHIVED("ARCHIVED", "Lưu trữ");

    public String code;
    public String name;

}
