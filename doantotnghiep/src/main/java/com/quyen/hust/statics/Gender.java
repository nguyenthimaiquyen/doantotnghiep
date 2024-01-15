package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("MALE", "nam"),
    FEMALE( "FEMALE", "nữ"),
    OTHER("OTHER", "khác");

    public String code;
    public String name;
}
