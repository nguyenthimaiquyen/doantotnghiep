package com.quyen.hust.controller.webstatics;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.*;
import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.model.response.user.EnrollmentResponse;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.service.admin.TrainingFieldService;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.course.LessonService;
import com.quyen.hust.service.course.QuizService;
import com.quyen.hust.service.course.SectionService;
import com.quyen.hust.service.teacher.TeacherService;
import com.quyen.hust.service.user.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseWebController {
    private final CourseService courseService;
    private final SectionService sectionService;
    private final TrainingFieldService trainingFieldService;
    private final TeacherService teacherService;


    @GetMapping
    public String getCoursePage(Model model, CourseSearchRequest request) {
        CourseResponse courseResponse = courseService.searchCourse(request);
        List<TrainingFieldResponse> trainingFields = trainingFieldService.getAll();
        List<TeacherResponse> teachers = teacherService.getTeachers();
        model.addAttribute("trainingFields", trainingFields);
        model.addAttribute("teachers", teachers);
        model.addAttribute("courses", courseResponse.getCourses());
        model.addAttribute("requestSearch", request.getCourseName());
        model.addAttribute("currentPage", courseResponse.getCurrentPage());
        model.addAttribute("totalPage", courseResponse.getTotalPage());
        model.addAttribute("totalElement", courseResponse.getTotalElement());
        model.addAttribute("pageSize", courseResponse.getPageSize());
        return "course/courses";
    }

    @GetMapping("/{id}")
    public String getCourseDetailsPage(Model model, @PathVariable Long id) throws CourseNotFoundException {
        CourseDataResponse courseDetails = courseService.getCourseDetails(id);
        List<SectionResponse> sections = sectionService.getSections(id);
        model.addAttribute("course", courseDetails);
        model.addAttribute("sections", sections);
        return "course/course-details";
    }

    @GetMapping("/analysis")
    public String getAdminDashboardPage(Model model) {
        return "admin/dashboard/dashboard";
    }

    @GetMapping("/management")
    public String getCourseManagementPage() {
        return "course/course-management";
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
