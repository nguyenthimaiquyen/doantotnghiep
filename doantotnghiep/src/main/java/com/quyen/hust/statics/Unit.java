package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Unit {
    USD("USD", "USD"),
    VND( "VND", "VNĐ");

    public String code;
    public String name;
}
