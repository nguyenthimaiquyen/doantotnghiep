package com.quyen.hust.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("PAID", "đã thanh toán"),
    UNPAID( "UNPAID", "chưa thanh toán"),
    CANCELED("CANCELED", "hủy bỏ");

    public String code;
    public String name;
}
