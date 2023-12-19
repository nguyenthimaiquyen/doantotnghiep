package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DifficultyLevel {
    BEGINNER("BEGINNER", "mới bắt đầu"),
    INTERMEDIATE( "INTERMEDIATE", "trung cấp"),
    ADVANCED("ADVANCED", "nâng cao");

    public String code;
    public String name;
}
