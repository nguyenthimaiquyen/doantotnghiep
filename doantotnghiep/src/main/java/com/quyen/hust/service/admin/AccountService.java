package com.quyen.hust.service.admin;

import com.quyen.hust.model.request.SearchRequest;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.repository.admin.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public UserResponse searchUser(SearchRequest request) {
        List<UserDataResponse> data = accountRepository.searchUser(request);
        Long totalElement = 0L;
        if (!CollectionUtils.isEmpty(data)) {
            totalElement = data.get(0).getTotalRecord();
        }
        double totalPageTemp = (double) totalElement / request.getPageSize();
        if (totalPageTemp > totalElement / request.getPageSize()) {
            totalPageTemp++;
        }
        return UserResponse.builder()
                .users(data)
                .totalElement(totalElement)
                .totalPage(Double.valueOf(totalPageTemp).intValue())
                .currentPage(request.getCurrentPage())
                .pageSize(request.getPageSize())
                .build();
    }

}
