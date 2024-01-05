package com.quyen.hust.controller.anonymous;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AnonymousController {

    @GetMapping
    public String getHomePage(Model model) {
        return "index";
    }

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        return "layout/about";
    }

    @GetMapping("/courses")
    public String getCoursePage(Model model) {
        return "course/courses";
    }

    @GetMapping("/courses/details")
    public String getCourseDetailsPage(Model model) {
        return "course/course-details";
    }

    @GetMapping("/instructors")
    public String getInstructorPage(Model model) {
        return "instructor/instructors";
    }

    @GetMapping("/myLearning")
    public String getMyLearningPage(Model model) {
        return "myLearning/myLearning";
    }

    @GetMapping("/contact")
    public String getContactPage(Model model) {
        return "layout/contact";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "account/login";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        return "account/signup";
    }

    @GetMapping("/faq")
    public String getFAQPage(Model model) {
        return "layout/faq";
    }

    @GetMapping("/showPage403")
    public String getErrorPage(Model model) {
        return "error/403";
    }

}
