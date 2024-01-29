package com.quyen.hust.model.request.admin;

import com.quyen.hust.statics.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusRequest {

    private UserStatus userStatus;

}
