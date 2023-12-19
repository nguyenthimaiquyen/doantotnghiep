package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Frequency {

    ONCE("ONCE", "một lần"),
    DAILY( "DAILY", "hàng ngày"),
    WEEKLY("WEEKLY", "hàng tuần");

    public String code;
    public String name;
}
