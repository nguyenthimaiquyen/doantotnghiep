package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    CREATED("CREATED", "Đã tạo"),
    ACTIVATED("ACTIVATED", "Kích hoạt"),
    DEACTIVATED("DEACTIVATED", "Vô hiệu hóa"),
    BANNED("BANNED", "Bị cấm");

    public String code;
    public String name;
}
