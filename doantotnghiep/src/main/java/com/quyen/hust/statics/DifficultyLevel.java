package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DifficultyLevel {
    BEGINNER("BEGINNER", "Mới bắt đầu"),
    INTERMEDIATE( "INTERMEDIATE", "Trung cấp"),
    ADVANCED("ADVANCED", "Nâng cao");

    public String code;
    public String name;
}
