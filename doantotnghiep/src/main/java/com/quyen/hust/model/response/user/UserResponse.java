package com.quyen.hust.model.response.user;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private List<UserDataResponse> users;
    private Long totalElement;
    private int totalPage;
    private int currentPage;
    private int pageSize;

}
