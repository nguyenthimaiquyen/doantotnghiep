package com.quyen.hust.model.request.user;

import com.quyen.hust.model.request.BaseSearchRequest;
import lombok.Data;

@Data
public class UserSearchRequest extends BaseSearchRequest {

    String name;

    String email;

    Boolean activated;

    String gender;

}
