package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {
    USER("USER", "Học viên"),
    TEACHER("TEACHER", "Giảng viên"),
    ADMIN("ADMIN", "Admin");

    public String code;
    public String name;
}
