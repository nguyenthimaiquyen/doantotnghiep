package com.quyen.hust.controller.webstatics;

import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.service.course.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CommonWebController {
    private final CourseService courseService;

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<CourseDataResponse> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "index";
    }

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        return "layout/about";
    }

    @GetMapping("/contact")
    public String getContactPage(Model model) {
        return "layout/contact";
    }

    @GetMapping("/faq")
    public String getFAQPage(Model model) {
        return "layout/faq";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        return "account/signup";
    }
}
