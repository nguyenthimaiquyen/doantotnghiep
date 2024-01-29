package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Lesson;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import com.quyen.hust.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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


    public LessonResponse getLessonDetails(Long lessonId) throws LessonNotFoundException {
        Section section = lessonJpaRepository.findById(lessonId).get().getSection();
        List<Section> sections = sectionJpaRepository.findByCourseId(section.getCourse().getId());
        return lessonJpaRepository.findById(lessonId).map(
                lesson -> LessonResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .embeddedUrl(lesson.getEmbeddedUrl())
                        .fileUrl(lesson.getFileUrl())
                        .videoUrl(lesson.getVideoUrl())
                        .sectionId(section.getId())
                        .sectionTitle(section.getTitle())
                        .build()
        ).orElseThrow(() -> new LessonNotFoundException("Lesson with id " + lessonId + " could not be found!"));
    }

//    public List<LessonResponse> getLessons(Long sectionId) {
//        return lessonJpaRepository.findBySectionId(sectionId).stream().map(
//                lesson -> LessonResponse.builder()
//                        .id(lesson.getId())
//                        .title(lesson.getTitle())
//                        .content(lesson.getContent())
//                        .embeddedUrl(lesson.getEmbeddedUrl())
//                        .videoUrl(lesson.getVideoUrl())
//                        .fileUrl(lesson.getFileUrl())
//                        .build()
//        ).collect(Collectors.toList());
//    }

    public void saveLesson(LessonRequest request, MultipartFile file, MultipartFile video) throws UnsupportedFormatException {
        if (file != null) {
            //kiểm tra định dạng file thỏa mãn: pdf, xls, xlsx, doc, docx, ppt, pptx
            if (!FileUtil.isValidFileFormat(file.getOriginalFilename())) {
                throw new UnsupportedFormatException("File formats do not support");
            }
            //lưu file
            String filePath = "course_data" + File.separator + "file" + File.separator + FileUtil.getFileNameWithTime(file.getOriginalFilename());
            try (InputStream fileInputStream = file.getInputStream()) {
                Files.copy(fileInputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (video != null) {
            //kiểm tra định dạng video tải lên
            if (!FileUtil.isValidVideoFormat(video.getOriginalFilename())) {
                throw new UnsupportedFormatException("Video formats do not support");
            }
            //lưu video
            String videoPath = "course_data" + File.separator + "video" + File.separator + FileUtil.getFileNameWithTime(video.getOriginalFilename());
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
                .build();
        if (file != null) {
            lesson.setFileUrl(file.getOriginalFilename());
        }
        if (video != null) {
            lesson.setVideoUrl(video.getOriginalFilename());
        }

        if (!ObjectUtils.isEmpty(request.getId())) {
            //update lesson
            Lesson lessonNeedUpdate = lessonJpaRepository.findById(request.getId()).get();
            lessonNeedUpdate.setTitle(request.getTitle());
            lessonNeedUpdate.setContent(request.getContent());
            lessonNeedUpdate.setEmbeddedUrl(request.getEmbeddedUrl());
            if (file != null) {
                lessonNeedUpdate.setFileUrl(file.getOriginalFilename());
            }
            if (video != null) {
                lessonNeedUpdate.setVideoUrl(video.getOriginalFilename());
            }
            lessonJpaRepository.save(lessonNeedUpdate);
            return;
        }
        //create lesson
        lessonJpaRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) {
        Optional<Lesson> lessonOptional = lessonJpaRepository.findById(lessonId);
        if (!lessonOptional.isPresent()) {
            return;
        }
        Section section = lessonOptional.get().getSection();
        List<Lesson> lessons = lessonJpaRepository.findBySectionId(section.getId());
        lessons.removeIf(lesson -> lesson.getId().equals(lessonId));
        sectionJpaRepository.save(section);
        lessonJpaRepository.delete(lessonOptional.get());
    }
}
