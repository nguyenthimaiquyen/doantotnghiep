package com.quyen.hust.service.course;


import com.quyen.hust.entity.admin.DiscountCode;
import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.entity.user.Enrollment;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.request.course.CourseStatusRequest;
import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.admin.DiscountCodeDataResponse;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.model.response.course.*;
import com.quyen.hust.repository.admin.DiscountCodeJpaRepository;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.CourseRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.EnrollmentJpaRepository;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.statics.CourseStatus;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import com.quyen.hust.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseJpaRepository courseJpaRepository;
    private final CourseRepository courseRepository;
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;
    private final DiscountCodeJpaRepository discountCodeJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final SectionJpaRepository sectionJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final EnrollmentJpaRepository enrollmentJpaRepository;

    public List<CourseFeeUnitResponse> getCourseFeeUnit() {
        return List.of(
                CourseFeeUnitResponse.builder().code(Unit.USD.getCode()).name(Unit.USD.getName()).build(),
                CourseFeeUnitResponse.builder().code(Unit.VND.getCode()).name(Unit.VND.getName()).build()
        );
    }

    public List<DifficultyLevelResponse> getDifficultyLevel() {
        return List.of(
                DifficultyLevelResponse.builder().code(DifficultyLevel.BEGINNER.getCode()).name(DifficultyLevel.BEGINNER.getName()).build(),
                DifficultyLevelResponse.builder().code(DifficultyLevel.INTERMEDIATE.getCode()).name(DifficultyLevel.INTERMEDIATE.getName()).build(),
                DifficultyLevelResponse.builder().code(DifficultyLevel.ADVANCED.getCode()).name(DifficultyLevel.ADVANCED.getName()).build()
        );
    }


    public List<TrainingFieldResponse> getTrainingField() {
        return trainingFieldJpaRepository.findAll().stream().map(
                trainingField -> TrainingFieldResponse.builder()
                        .id(trainingField.getId())
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<DiscountCodeDataResponse> getDiscountCode() {
        return discountCodeJpaRepository.findAll().stream().map(
                discountCode -> DiscountCodeDataResponse.builder()
                        .id(discountCode.getId())
                        .codeName(discountCode.getCodeName())
                        .build()
        ).collect(Collectors.toList());
    }

    public void saveCourse(CourseRequest request, MultipartFile image) throws UnsupportedFormatException {
        String imageDB = null;
        if (image != null) {
            //kiểm tra định dạng image tải lên
            if (!FileUtil.isValidImageFormat(image.getOriginalFilename())) {
                throw new UnsupportedFormatException("Image formats do not support");
            }
            //lưu ảnh
            imageDB = FileUtil.getFileNameWithTime(image.getOriginalFilename());
            String imagePath = "course_data" + File.separator + "image" + File.separator + imageDB;
            try (InputStream videoInputStream = image.getInputStream()) {
                Files.copy(videoInputStream, Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //get teacher, discount code
        Teacher teacher;
        if (request.getTeacher() != null) {
            teacher = teacherJpaRepository.findById(request.getTeacher()).get();
        } else {
            Optional<Long> id = SecurityUtils.getCurrentUserLoginId();
            teacher = teacherJpaRepository.findByUserId(id.get());
        }
        Optional<DiscountCode> discountCode = Optional.empty();
        if (request.getDiscountCode() != null) {
            discountCode = discountCodeJpaRepository.findById(request.getDiscountCode());
        }
        List<TrainingField> trainingFields = trainingFieldJpaRepository.findAllById(request.getTrainingFields());
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .learningObjectives(request.getLearningObjectives())
                .courseFee(request.getCourseFee())
                .courseFeeUnit(request.getCourseFeeUnit())
                .difficultyLevel(request.getDifficultyLevel())
                .trainingField(trainingFields)
                .teacher(teacher)
                .discountCode(discountCode.orElse(null))
                .imageUrl(imageDB)
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update course
            Course courseNeedUpdate = courseJpaRepository.findById(request.getId()).orElse(null);
            courseNeedUpdate.setTitle(request.getTitle());
            courseNeedUpdate.setDescription(request.getDescription());
            courseNeedUpdate.setLearningObjectives(request.getLearningObjectives());
            courseNeedUpdate.setCourseFee(request.getCourseFee());
            courseNeedUpdate.setCourseFeeUnit(request.getCourseFeeUnit());
            courseNeedUpdate.setDifficultyLevel(request.getDifficultyLevel());
            courseNeedUpdate.setTrainingField(trainingFields);
            courseNeedUpdate.setTeacher(teacher);
            courseNeedUpdate.setDiscountCode(discountCode.orElse(null));
            courseNeedUpdate.setImageUrl(imageDB);
            courseJpaRepository.save(courseNeedUpdate);
            return;
        }
        //create a new course
        course.setCourseStatus(CourseStatus.DRAFT);
        courseJpaRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Optional<Course> courseOptional = courseJpaRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Section> sections = sectionJpaRepository.findByCourseId(course.getId());
            for (Section section : sections) {
                List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
                lessonJpaRepository.deleteAll(lessons);
            }
            sectionJpaRepository.deleteAll(sections);
        }
        courseJpaRepository.deleteById(id);
    }


//    public List<CourseDataResponse> getAll() {
//        //dùng hàm search,
//        return courseJpaRepository.findAll().stream().map(
//                course -> CourseDataResponse.builder()
//                        .id(course.getId())
//                        .title(course.getTitle())
//                        .description(course.getDescription())
//                        .learningObjectives(course.getLearningObjectives())
//                        .courseFee(course.getCourseFee())
//                        .courseFeeUnit(course.getCourseFeeUnit())
//                        .difficultyLevel(course.getDifficultyLevel())
//                        .courseStatus(course.getCourseStatus())
//                        .build()
//        ).collect(Collectors.toList());
//    }

    public CourseDataResponse getCourseDetails(Long id) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseJpaRepository.findById(id);
        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course with id " + id + " could not be found!");
        }

        List<Section> sections = sectionJpaRepository.findByCourseId(id);
        int totalLessons = 0;
        int totalQuizzes = 0;
        int courseTotalHours = 0;
        int courseTotalMinutes = 0;
        int courseTotalSeconds = 0;
        for (Section section : sections) {
            totalLessons += section.getTotalLessons();
            totalQuizzes += section.getTotalQuizzes();
            String[] timeParts = section.getTotalTime().split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);
            courseTotalMinutes += minutes;
            courseTotalSeconds += seconds;
        }
        int overflowSeconds = courseTotalSeconds / 60;
        courseTotalMinutes += overflowSeconds;
        courseTotalSeconds = courseTotalSeconds % 60;
        courseTotalHours = courseTotalMinutes / 60;
        courseTotalMinutes = courseTotalMinutes % 60;
        String totalTime = String.format("%dh:%02dm:%02ds", courseTotalHours, courseTotalMinutes, courseTotalSeconds);

        Course courseNeedUpdate = courseOptional.get();
        courseNeedUpdate.setTotalLessons(totalLessons);
        courseNeedUpdate.setTotalQuizzes(totalQuizzes);
        courseNeedUpdate.setTotalTime(totalTime);
        courseJpaRepository.save(courseNeedUpdate);

        return courseJpaRepository.findById(id).map(
                course -> {
                    CourseDataResponse.CourseDataResponseBuilder builder = CourseDataResponse.builder()
                            .id(course.getId())
                            .title(course.getTitle())
                            .description(course.getDescription())
                            .learningObjectives(course.getLearningObjectives())
                            .courseFee(course.getCourseFee())
                            .courseFeeUnit(course.getCourseFeeUnit())
                            .imageUrl(course.getImageUrl())
                            .learnerCount(course.getLearnerCount())
                            .rating(course.getRating())
                            .difficultyLevel(course.getDifficultyLevel())
                            .courseStatus(course.getCourseStatus())
                            .teacherId(course.getTeacher().getId())
                            .teacherName(course.getTeacher().getUser().getFullName())
                            .totalLessons(course.getTotalLessons())
                            .totalQuizzes(course.getTotalQuizzes())
                            .totalTime(course.getTotalTime())
                            .trainingFieldsId(course.getTrainingField().stream().map(
                                    trainingField -> trainingField.getId()
                                    ).collect(Collectors.toList())
                            ).trainingFields(course.getTrainingField().stream().map(
                                    trainingField -> trainingField.getFieldName()
                            ).collect(Collectors.toList()));
                    if (course.getDiscountCode() != null) {
                        builder.discountCodeName(course.getDiscountCode().getCodeName());
                        builder.discountCodeId(course.getDiscountCode().getId());
                    } else {
                        builder.discountCodeName(null);
                    }
                    return builder.build();
                }).get();
    }

    public void changeCourseStatus(Long courseId, CourseStatusRequest status) throws CourseNotFoundException {
        Course course = courseJpaRepository.findById(courseId).orElse(null);
        if (ObjectUtils.isEmpty(course)) {
            throw new CourseNotFoundException("Course with id " + courseId + " could not be found!");
        }
        if (status.equals("PUBLISHED")) {
            Random random = new Random();
            //sinh số người học của course
            Long learnerCount = 100 + random.nextLong(900);
            course.setLearnerCount(learnerCount);
            //sinh số sao cho course
            Double starCount = 3 + random.nextDouble(2);
            course.setRating(starCount);
        }
        course.setCourseStatus(status.getCourseStatus());
        courseJpaRepository.save(course);
    }


    public CourseResponse searchCourse(CourseSearchRequest request) {
        List<CourseDataResponse> data = courseRepository.searchCourse(request);
        Long totalElement = 0L;
        if (!CollectionUtils.isEmpty(data)) {
            totalElement = data.get(0).getTotalRecord();
        }
        double totalPageTemp = (double) totalElement / request.getPageSize();
        if (totalPageTemp > totalElement / request.getPageSize()) {
            totalPageTemp++;
        }
        return CourseResponse.builder()
                .courses(data)
                .totalElement(totalElement)
                .totalPage(Double.valueOf(totalPageTemp).intValue())
                .currentPage(request.getPageIndex())
                .pageSize(request.getPageSize())
                .build();
    }

    public LessonInfo getLessonInfo(Long courseId, Long lessonId, String action) {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if ("previous".equals(action)) {
            List<Section> sections = sectionJpaRepository.findByCourseId(courseId);
            for (int i = 0; i < sections.size(); i++) {
                List<Lesson> lessons = lessonJpaRepository.findBySectionId(sections.get(i).getId());
                Lesson lesson1 = lessons.stream().filter(lesson -> lesson.getId() == lessonId).findFirst().get();
                int currentIndex  = lessons.indexOf(lessonOptional.get());
                if (currentIndex == 0) {
                    return LessonInfo.builder()
                            .courseId(courseId)
                            .lessonId(lessons.get(1).getId())
                            .build();
                } else if (currentIndex > 0 && currentIndex < lessons.size() - 1) {
                    return LessonInfo.builder()
                            .courseId(courseId)
                            .lessonId(lessons.get(1).getId())
                            .build();
                }

            }
            return null;
        } else if ("next".equals(action)) {
            return null;
        }
        return null;
    }

    public List<CourseDataResponse> getEnrolledCourse(Long userId) {
        List<Enrollment> enrollments = enrollmentJpaRepository.findByUserId(userId);
        return enrollments.stream().map(
                enrollment -> CourseDataResponse.builder()
                        .id(enrollment.getCourse().getId())
                        .title(enrollment.getCourse().getTitle())
                        .courseFee(enrollment.getCourse().getCourseFee())
                        .courseFeeUnit(enrollment.getCourse().getCourseFeeUnit())
                        .trainingFields(enrollment.getCourse().getTrainingField().stream().map(
                                trainingField -> trainingField.getFieldName()
                        ).collect(Collectors.toList()))
                        .teacherName(enrollment.getCourse().getTeacher().getUser().getFullName())
                        .difficultyLevel(enrollment.getCourse().getDifficultyLevel())
                        .learnerCount(enrollment.getCourse().getLearnerCount())
                        .rating(enrollment.getCourse().getRating())
                        .imageUrl(enrollment.getCourse().getImageUrl())
                        .courseStatus(enrollment.getCourse().getCourseStatus())
                        .build()
        ).collect(Collectors.toList());
    }


}
