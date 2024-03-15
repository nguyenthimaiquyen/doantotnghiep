package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Quiz;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.model.request.course.VideoDurationRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.QuizJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {
    private final SectionJpaRepository sectionJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final QuizJpaRepository quizJpaRepository;


    public LessonResponse getLessonDetails(Long lessonId) throws LessonNotFoundException {
        Lesson lesson = lessonJpaRepository.findById(lessonId).orElseThrow(
                () -> new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!"));
        Section currentSection = lesson.getSection();
        List<Lesson> lessons = lessonJpaRepository.findBySectionId(currentSection.getId());
        Long nextLessonId = null;
        Long previousLessonId = null;
        Long previousQuizId = null;
        Long nextQuizId = null;
        int currentLessonIndex = lessons.indexOf(lesson);
        //tìm id của previous lesson, previous quiz
        if (currentLessonIndex == 0) {
            List<Section> sections = sectionJpaRepository.findByCourseId(currentSection.getCourse().getId());
            int currentSectionIndex = sections.indexOf(currentSection);
            if (currentSectionIndex > 0) {
                Section previousSection = sections.get(currentSectionIndex - 1);
                List<Quiz> previousSectionQuizzes = quizJpaRepository.findBySectionId(previousSection.getId());
                if (!previousSectionQuizzes.isEmpty()) {
                    previousQuizId = previousSectionQuizzes.get(previousSectionQuizzes.size() - 1).getId();
                }
                List<Lesson> previousSectionLessons = lessonJpaRepository.findBySectionId(previousSection.getId());
                if (!previousSectionLessons.isEmpty()) {
                    previousLessonId = previousSectionLessons.get(previousSectionLessons.size() - 1).getId();
                }
            }
        } else {
            Lesson previousLesson = lessons.get(currentLessonIndex - 1);
            previousLessonId = previousLesson.getId();
        }
        //tìm id của next lesson, next quiz
        if (currentLessonIndex == (lessons.size() - 1)) {
            List<Quiz> quizzes = quizJpaRepository.findBySectionId(currentSection.getId());
            if (!CollectionUtils.isEmpty(quizzes)) {
                nextQuizId = quizzes.get(0).getId();
            }
            List<Section> sections = sectionJpaRepository.findByCourseId(currentSection.getCourse().getId());
            int currentSectionIndex = sections.indexOf(currentSection);
            if (currentSectionIndex < sections.size() - 1) {
                Section nextSection = sections.get(currentSectionIndex + 1);
                List<Lesson> nextSectionLessons = lessonJpaRepository.findBySectionId(nextSection.getId());
                if (!nextSectionLessons.isEmpty()) {
                    nextLessonId = nextSectionLessons.get(0).getId();
                }
            }
        } else {
            Lesson nextLesson = lessons.get(currentLessonIndex + 1);
            nextLessonId = nextLesson.getId();
        }
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .embeddedUrl(lesson.getEmbeddedUrl())
                .fileUrl(lesson.getFileUrl())
                .videoUrl(lesson.getVideoUrl())
                .sectionId(currentSection.getId())
                .sectionTitle(currentSection.getTitle())
                .activated(lesson.getActivated())
                .videoDuration(lesson.getVideoDuration())
                .nextLessonId(nextLessonId)
                .previousLessonId(previousLessonId)
                .previousQuizId(previousQuizId)
                .nextQuizId(nextQuizId)
                .build();
    }

    public void saveLesson(LessonRequest request, MultipartFile file, MultipartFile video) throws UnsupportedFormatException {
        String fileDB = null;
        if (file != null) {
            //kiểm tra định dạng file thỏa mãn: pdf, xls, xlsx, doc, docx, ppt, pptx
            if (!FileUtil.isValidFileFormat(file.getOriginalFilename())) {
                throw new UnsupportedFormatException("File formats do not support");
            }
            //lưu file
            fileDB = FileUtil.getFileNameWithTime(file.getOriginalFilename());
            String filePath = "course_data" + File.separator + "file" + File.separator + fileDB;
            try (InputStream fileInputStream = file.getInputStream()) {
                Files.copy(fileInputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String videoDB = null;
        String videoPath = null;
        if (video != null) {
            //kiểm tra định dạng video tải lên
            if (!FileUtil.isValidVideoFormat(video.getOriginalFilename())) {
                throw new UnsupportedFormatException("Video formats do not support");
            }
            //lưu video
            videoDB = FileUtil.getFileNameWithTime(video.getOriginalFilename());
            videoPath = "course_data" + File.separator + "video" + File.separator + videoDB;
            try (InputStream videoInputStream = video.getInputStream()) {
                Files.copy(videoInputStream, Paths.get(videoPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .section(sectionOptional.get())
                .embeddedUrl(request.getEmbeddedUrl())
                .preview(false)
                .build();
        if (file != null) {
            lesson.setFileUrl(fileDB);
        }
        if (video != null) {
            lesson.setVideoUrl(videoDB);
        }

        if (!ObjectUtils.isEmpty(request.getId())) {
            //update lesson
            Lesson lessonNeedUpdate = lessonJpaRepository.findById(request.getId()).get();
            lessonNeedUpdate.setTitle(request.getTitle());
            lessonNeedUpdate.setContent(request.getContent());
            lessonNeedUpdate.setEmbeddedUrl(request.getEmbeddedUrl());
            if (file != null) {
                lessonNeedUpdate.setFileUrl(fileDB);
            }
            if (video != null) {
                lessonNeedUpdate.setVideoUrl(videoDB);
            }
            lessonJpaRepository.save(lessonNeedUpdate);
            return;
        }
        //create lesson
        lessonJpaRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) throws LessonNotFoundException {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if (!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!");
        }
        Section section = lessonOptional.get().getSection();
        List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
        lessons.removeIf(lesson -> lesson.getId().equals(lessonId));
        sectionJpaRepository.save(section);
        lessonJpaRepository.delete(lessonOptional.get());
    }

    public void saveVideoDuration(VideoDurationRequest videoDurationRequest) throws LessonNotFoundException {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(videoDurationRequest.getLessonId());
        if (!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson with id " + videoDurationRequest.getLessonId() + " could not be found!");
        }
        Lesson lesson = lessonOptional.get();
        lesson.setVideoDuration(videoDurationRequest.getVideoDuration());
        lessonJpaRepository.save(lesson);
    }

    public Integer getLessonIndex(Long lessonId) throws LessonNotFoundException {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if (!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!");
        }
        Section section = lessonOptional.get().getSection();
        List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getId() == lessonId) {
                return i;
            }
        }
        return null;
    }

    public Integer getSectionIndex(Long lessonId) throws LessonNotFoundException {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if (!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!");
        }
        Section section = lessonOptional.get().getSection();
        List<Section> sections = sectionJpaRepository.findByCourseId(section.getCourse().getId());
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getId() == section.getId()) {
                return i;
            }
        }
        return null;
    }
}
