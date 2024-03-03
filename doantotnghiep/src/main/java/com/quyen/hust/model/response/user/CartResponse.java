package com.quyen.hust.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private Long id;

    private Long userId;

    private List<CartItemResponse> cartItems;

    private Double totalFee;

}
