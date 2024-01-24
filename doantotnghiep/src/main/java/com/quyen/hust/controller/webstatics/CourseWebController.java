package com.quyen.hust.controller.webstatics;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.SectionResponse;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.course.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseWebController {
    private final CourseService courseService;
    private final SectionService sectionService;

    @GetMapping("/analysis")
    public String getAdminDashboardPage(Model model) {
        return "admin/dashboard/dashboard";
    }

    @GetMapping("/management")
    public String getCourseManagementPage(Model model) {
        List<CourseDataResponse> courses = courseService.getAll();
//        Optional<Role> currentUserLoginRole = SecurityUtils.getCurrentUserLoginRole();
        model.addAttribute("courses", courses);
//        model.addAttribute("role", currentUserLoginRole.get().getName().name());
        return "course/course-management";
    }

    @GetMapping("/creation")
    public String getCourseCreationPage(Model model) {
        return "course/course-creation";
    }

    @GetMapping("/{id}/sections")
    public String getSectionCreationPage(Model model, @PathVariable Long id) throws CourseNotFoundException {
        List<SectionResponse> sections = sectionService.getSections(id);
        CourseDataResponse courseDetails = courseService.getCourseDetails(id);
        model.addAttribute("courseStatus", courseDetails.getCourseStatus());
        model.addAttribute("courseTitle", courseDetails.getTitle());
        model.addAttribute("sections", sections);
        return "course/section-creation";
    }


}
