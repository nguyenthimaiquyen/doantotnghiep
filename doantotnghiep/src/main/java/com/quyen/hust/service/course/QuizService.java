package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.*;
import com.quyen.hust.entity.course.Quiz;
import com.quyen.hust.exception.AnswerNotFoundException;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.QuizRequest;
import com.quyen.hust.model.response.course.AnswerResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import com.quyen.hust.repository.course.AnswerJpaRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.QuizJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class QuizService {
    private final SectionJpaRepository sectionJpaRepository;
    private final QuizJpaRepository quizJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;

    public QuizResponse getQuizDetails(Long quizId) throws QuizNotFoundException {
        Quiz currentQuiz = quizJpaRepository.findById(quizId).orElseThrow(
                () -> new QuizNotFoundException("Quiz with id " + quizId + " could not be found!"));
        Section currentSection = currentQuiz.getSection();
        List<Quiz> quizzes = quizJpaRepository.findBySectionId(currentSection.getId());
        Long nextQuizId = null;
        Long previousQuizId = null;
        Long nextLessonId = null;
        Long previousLessonId = null;
        int currentQuizIndex = quizzes.indexOf(currentQuiz);
        //tìm id của previous quiz, previous lessons
        if (currentQuizIndex == 0) {
            List<Lesson> currentSectionLessons = lessonJpaRepository.findBySectionId(currentSection.getId());
            if (!currentSectionLessons.isEmpty()) {
                previousLessonId = currentSectionLessons.get(currentSectionLessons.size() - 1).getId();
            }
        } else {
            Quiz previousQuiz = quizzes.get(currentQuizIndex - 1);
            previousQuizId = previousQuiz.getId();
        }
        //tìm id của next quizze, next lessons
        if (currentQuizIndex == (quizzes.size() - 1)) {
            List<Section> sections = sectionJpaRepository.findByCourseId(currentSection.getCourse().getId());
            int currentSectionIndex = sections.indexOf(currentSection);
            if (currentSectionIndex < sections.size() - 1) {
                Section nextSection = sections.get(currentSectionIndex + 1);
                List<Quiz> nextSectionQuizzes = quizJpaRepository.findBySectionId(nextSection.getId());
                if (!nextSectionQuizzes.isEmpty()) {
                    nextQuizId = nextSectionQuizzes.get(0).getId();
                }
                List<Lesson> nextSectionLessons = lessonJpaRepository.findBySectionId(nextSection.getId());
                if (!nextSectionLessons.isEmpty()) {
                    nextLessonId = nextSectionLessons.get(0).getId();
                }
            }
        } else {
            Quiz nextQuiz = quizzes.get(currentQuizIndex + 1);
            nextQuizId = nextQuiz.getId();
        }

        List<Answer> answers = answerJpaRepository.findByQuizId(quizId);
        List<AnswerResponse> answerResponses = answers.stream().map(
                answer -> AnswerResponse.builder()
                        .id(answer.getId())
                        .content(answer.getContent())
                        .isCorrect(answer.isCorrect())
                        .build()
        ).collect(Collectors.toList());
        return QuizResponse.builder()
                .id(currentQuiz.getId())
                .title(currentQuiz.getTitle())
                .question(currentQuiz.getQuestion())
                .explanation(currentQuiz.getExplanation())
                .answers(answerResponses)
                .sectionId(currentSection.getId())
                .sectionTitle(currentSection.getTitle())
                .nextQuizId(nextQuizId)
                .previousQuizId(previousQuizId)
                .nextLessonId(nextLessonId)
                .previousLessonId(previousLessonId)
                .build();
    }

    @Transactional
    public void saveQuiz(QuizRequest request) throws SectionNotFoundException, AnswerNotFoundException {
        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        if (!sectionOptional.isPresent()) {
            throw new SectionNotFoundException("Section with id " + request.getSectionId() + " could not be found!");
        }
        if (request.getAnswers().isEmpty()) {
            throw new AnswerNotFoundException("List of answers is empty!");
        }
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update quiz
            Quiz quizNeedUpdate = quizJpaRepository.findById(request.getId()).get();
            quizNeedUpdate.setTitle(request.getTitle());
            quizNeedUpdate.setQuestion(request.getQuestion());
            quizNeedUpdate.setExplanation(request.getExplanation());
            List<Answer> answers = request.getAnswers().stream().map(
                    answerRequest -> Answer.builder()
                            .content(answerRequest.getContent())
                            .isCorrect(answerRequest.getIsCorrect())
                            .quiz(quizNeedUpdate)
                            .build()
            ).collect(Collectors.toList());
            answerJpaRepository.deleteByQuizId(quizNeedUpdate.getId());
            answerJpaRepository.saveAll(answers);
            quizJpaRepository.save(quizNeedUpdate);
        } else {
            //sinh số thời gian đọc quiz ngẫu nhiên từ 1p đến 3p
            Random random = new Random();
            int minutes = 1 + random.nextInt(2);
            int seconds = random.nextInt(60);
            String timeCount = minutes + ":" + seconds;
            //create quiz
            Quiz quiz = Quiz.builder()
                    .title(request.getTitle())
                    .question(request.getQuestion())
                    .explanation(request.getExplanation())
                    .section(sectionOptional.get())
                    .timeCount(timeCount)
                    .build();
            List<Answer> answers = request.getAnswers().stream().map(
                    answerRequest -> Answer.builder()
                            .content(answerRequest.getContent())
                            .isCorrect(answerRequest.getIsCorrect())
                            .quiz(quiz)
                            .build()
            ).collect(Collectors.toList());
            quizJpaRepository.save(quiz);
            answerJpaRepository.saveAll(answers);
        }
    }

    public void deleteQuiz(Long quizId) throws QuizNotFoundException {
        Optional<Quiz> quiz = quizJpaRepository.findById(quizId);
        if (!quiz.isPresent()) {
            throw new QuizNotFoundException("Quiz with id " + quizId + " could not be found");
        }
        List<Answer> answers = answerJpaRepository.findByQuizId(quiz.get().getId());
        answerJpaRepository.deleteAll(answers);
        quizJpaRepository.deleteById(quizId);
    }

}



