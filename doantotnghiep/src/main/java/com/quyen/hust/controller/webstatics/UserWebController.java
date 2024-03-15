package com.quyen.hust.controller.webstatics;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import com.quyen.hust.model.response.course.SectionResponse;
import com.quyen.hust.model.response.user.CartResponse;
import com.quyen.hust.model.response.user.EnrollmentResponse;
import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.model.response.user.WishlistResponse;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.course.LessonService;
import com.quyen.hust.service.course.QuizService;
import com.quyen.hust.service.course.SectionService;
import com.quyen.hust.service.user.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserWebController {
    private final StudyReminderService studyReminderService;
    private final UserService userService;
    private final CartService cartService;
    private final WishlistService wishlistService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;
    private final QuizService quizService;
    private final SectionService sectionService;


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

    @GetMapping("/teachers")
    public String getInstructorPage(Model model) {
        return "teacher/teachers";
    }

    @GetMapping("/carts/{userId}")
    public String getCartPage(Model model, @PathVariable Long userId) {
        CartResponse cartResponse = cartService.getCart(userId);
        model.addAttribute("cartItems", cartResponse.getCartItems());
        model.addAttribute("cartId", cartResponse.getId());
        model.addAttribute("totalCourses", cartResponse.getCartItems().size());
        model.addAttribute("totalFee", cartResponse.getTotalFee());
        return "cart/cart";
    }

    @GetMapping("/wishlists/{userId}")
    public String getWishlistPage(Model model, @PathVariable Long userId) {
        WishlistResponse wishlistResponse = wishlistService.getWishlist(userId);
        model.addAttribute("wishlistItems", wishlistResponse.getWishlistItems());
        model.addAttribute("totalCourses", wishlistResponse.getWishlistItems().size());
        return "wishlist/wishlist";
    }

    @GetMapping("/payments")
    public String getHomePage(Model model) {
        return "cart/checkout";
    }

    @GetMapping("/reminders/{userId}")
    public String getReminderPage(Model model, @PathVariable Long userId) {
        List<CourseDataResponse> courses = courseService.getEnrolledCourse(userId);
        List<StudyReminderResponse> studyReminders = studyReminderService.getReminders(userId);
        model.addAttribute("courses", courses);
        model.addAttribute("studyReminders", studyReminders);
        return "my-learning/my-learning";
    }

    @GetMapping("/accounts/{id}/activation")
    public String getAccountActivationPage(@PathVariable Long id) {
        userService.verifyAccount(id);
        return "layout/account-activation";
    }

    @GetMapping("/users/{userId}/courses/{courseId}/lessons/{lessonId}")
    public String getLessonDetailsPage(Model model, @PathVariable Long userId, @PathVariable Long courseId,
                                       @PathVariable Long lessonId) throws CourseNotFoundException, LessonNotFoundException, QuizNotFoundException {
        EnrollmentResponse enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userId, courseId);
        CourseDataResponse courseDetails = courseService.getCourseDetails(courseId);
        List<SectionResponse> sections = sectionService.getSections(courseId);
        LessonResponse lesson = lessonService.getLessonDetails(lessonId);
        model.addAttribute("course", courseDetails);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("sections", sections);
        model.addAttribute("lesson", lesson);
        model.addAttribute("userId", userId);
        return "course/lesson-details";
    }

    @GetMapping("/users/{userId}/courses/{courseId}/quizzes/{quizId}")
    public String getQuizDetailsPage(Model model, @PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long quizId)
            throws CourseNotFoundException, QuizNotFoundException {
        EnrollmentResponse enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userId, courseId);
        CourseDataResponse courseDetails = courseService.getCourseDetails(courseId);
        List<SectionResponse> sections = sectionService.getSections(courseId);
        QuizResponse quiz = quizService.getQuizDetails(quizId);
        model.addAttribute("course", courseDetails);
        model.addAttribute("sections", sections);
        model.addAttribute("quiz", quiz);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("userId", userId);
        return "course/quiz-details";
    }

}
