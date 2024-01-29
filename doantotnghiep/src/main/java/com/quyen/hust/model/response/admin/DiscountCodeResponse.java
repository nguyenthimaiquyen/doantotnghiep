package com.quyen.hust.model.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountCodeResponse {
    private List<DiscountCodeDataResponse> discountCodes;
    private Long totalElement;
    private int totalPage;
    private int currentPage;
    private int pageSize;
}
