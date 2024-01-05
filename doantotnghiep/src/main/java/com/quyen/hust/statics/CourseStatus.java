package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseStatus {

    DRAFT("DRAFT", "phác thảo"),
    WAITING_FOR_REVIEW( "WAITING_FOR_REVIEW", "chờ xem xét"),
    APPROVED("APPROVED", "chấp thuận"),
    REJECTED("REJECTED", "bị từ chối"),
    PUBLISH("PUBLISH", "đã công bố"),
    UNPUBLISHED("UNPUBLISHED", "hủy công bố"),
    ARCHIVED("ARCHIVED", "lưu trữ");

    public String code;
    public String name;
}
