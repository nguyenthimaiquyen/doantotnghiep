package com.quyen.hust.controller.webstatics;

import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.service.user.StudyReminderService;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserWebController {
    private final StudyReminderService studyReminderService;
    private final UserService userService;

    @GetMapping("/myLearning")
    public String getMyLearningPage(Model model) {
        return "myLearning/myLearning";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "account/login";
    }

    @GetMapping("/information")
    public String getInformationPage(Model model) {
        return "layout/information";
    }

    @GetMapping("/403")
    public String getErrorPage(Model model) {
        return "error/403";
    }

    @GetMapping("/courses")
    public String getCoursePage(Model model) {
        return "course/courses";
    }

    @GetMapping("/courses/details")
    public String getCourseDetailsPage(Model model) {
        return "course/course-details";
    }

    @GetMapping("/teachers")
    public String getInstructorPage(Model model) {
        return "teacher/teachers";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model) {
        return "cart/cart";
    }

    @GetMapping("/payments")
    public String getHomePage(Model model) {
        return "cart/checkout";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        return "profile/profile";
    }

    @GetMapping("/profile/otp-sending")
    public String getOtpSendingPage(Model model) {
        return "profile/otp-sending";
    }

    @GetMapping("/profile/forget-password")
    public String getForgetPasswordPage(Model model) {
        return "profile/forget-password";
    }

    @GetMapping("/reminders")
    public String getReminderPage(Model model) {
        List<StudyReminderResponse> studyReminders = studyReminderService.getAll();
        model.addAttribute("studyReminders", studyReminders);
        return "myLearning/myLearning";
    }

    @GetMapping("/wishlist")
    public String getWishlistPage(Model model) {
        return "wishlist/wishlist";
    }

    @GetMapping("/accounts/{id}/activation")
    public String getAccountActivationPage(@PathVariable Long id) {
        userService.verifyAccount(id);
        return "layout/account-activation";
    }

}
