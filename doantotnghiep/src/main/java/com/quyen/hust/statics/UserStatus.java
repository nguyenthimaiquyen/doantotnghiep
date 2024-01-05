package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    CREATED("CREATED", "đã tạo"),
    ACTIVATED("ACTIVATED", "kích hoạt"),
    DEACTIVATED("DEACTIVATED", "vô hiệu hóa"),
    BANNED("BANNED", "bị cấm");

    public String code;
    public String name;
}
