package com.quyen.hust.controller.webstatics;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.model.response.course.SectionResponse;
import com.quyen.hust.service.admin.DiscountCodeService;
import com.quyen.hust.service.admin.TrainingFieldService;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.course.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminWebController {
    private final TrainingFieldService trainingFieldService;
    private final DiscountCodeService discountCodeService;
    private final CourseService courseService;
    private final SectionService sectionService;


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
    public String getDiscountCodeManagementPage(Model model) {
        List<DiscountCodeResponse> discountCodes = discountCodeService.getAll();
        model.addAttribute("discountCodes", discountCodes);
        return "admin/discount-code/manage-discount-code";
    }

    @GetMapping("/accounts")
    public String getAccountManagementPage(Model model) {
        return "admin/account/manage-account";
    }

    @GetMapping("/courses/analysis/admin")
    public String getAdminDashboardPage(Model model) {
        return "admin/dashboard/dashboard";
    }

    @GetMapping("/courses/management")
    public String getCourseManagementPage(Model model) {
        List<CourseDataResponse> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "course/course-management";
    }

    @GetMapping("/courses/creation")
    public String getCourseCreationPage(Model model) {
        return "course/course-creation";
    }

    @GetMapping("/courses/{id}/sections")
    public String getSectionCreationPage(Model model, @PathVariable Long id) throws CourseNotFoundException {
        List<SectionResponse> sections = sectionService.getSections(id);
        CourseDataResponse courseDetails = courseService.getCourseDetails(id);
        model.addAttribute("courseTitle", courseDetails.getTitle());
        model.addAttribute("sections", sections);
        return "course/section-creation";
    }



}
