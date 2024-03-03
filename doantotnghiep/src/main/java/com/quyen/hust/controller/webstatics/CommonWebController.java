package com.quyen.hust.controller.webstatics;

import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.service.admin.TrainingFieldService;
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
    private final TrainingFieldService trainingFieldService;

    @GetMapping("/")
    public String getHomePage(Model model, CourseSearchRequest request) {
        CourseResponse courseResponse = courseService.searchCourse(request);
        model.addAttribute("requestSearch", request.getCourseName());
        model.addAttribute("courses", courseResponse.getCourses());
        model.addAttribute("trainingFields", trainingFieldService.getAll());
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
