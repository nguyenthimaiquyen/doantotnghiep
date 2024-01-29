package com.quyen.hust.controller.webstatics;

import com.quyen.hust.model.request.SearchRequest;
import com.quyen.hust.model.response.admin.DiscountCodeDataResponse;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.service.admin.AccountService;
import com.quyen.hust.service.admin.DiscountCodeService;
import com.quyen.hust.service.admin.TrainingFieldService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminWebController {
    private final TrainingFieldService trainingFieldService;
    private final DiscountCodeService discountCodeService;
    private final AccountService accountService;


    @GetMapping("/training-fields")
    public String getTrainingFieldManagementPage(Model model) {
        List<TrainingFieldResponse> trainingFields = trainingFieldService.getAll();
        model.addAttribute("trainingFields", trainingFields);
        return "admin/training-field/manage-training-field";
    }

    @GetMapping("/mail-sending")
    public String getMailSendingManagementPage(Model model) {
        return "admin/mail-sending/manage-mail-sending";
    }

    @GetMapping("/discount-codes")
    public String getDiscountCodeManagementPage(Model model, SearchRequest request) {
        DiscountCodeResponse discountCodeResponse = discountCodeService.searchDiscountCode(request);
        model.addAttribute("requestSearch", request);
        model.addAttribute("currentPage", discountCodeResponse.getCurrentPage());
        model.addAttribute("totalPage", discountCodeResponse.getTotalPage());
        model.addAttribute("totalElement", discountCodeResponse.getTotalElement());
        model.addAttribute("pageSize", discountCodeResponse.getPageSize());
        model.addAttribute("discountCodes", discountCodeResponse.getDiscountCodes());
        return "admin/discount-code/manage-discount-code";
    }

    @GetMapping("/accounts")
    public String getAccountManagementPage(Model model, SearchRequest request) {
        UserResponse userResponse = accountService.searchUser(request);
        model.addAttribute("requestSearch", request);
        model.addAttribute("currentPage", userResponse.getCurrentPage());
        model.addAttribute("totalPage", userResponse.getTotalPage());
        model.addAttribute("totalElement", userResponse.getTotalElement());
        model.addAttribute("pageSize", userResponse.getPageSize());
        model.addAttribute("accounts", userResponse.getUsers());
        return "admin/account/manage-account";
    }


}
