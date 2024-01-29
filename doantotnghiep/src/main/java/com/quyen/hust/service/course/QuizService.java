package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Answer;
import com.quyen.hust.entity.course.Quiz;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.QuizRequest;
import com.quyen.hust.model.response.course.AnswerResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import com.quyen.hust.repository.course.AnswerJpaRepository;
import com.quyen.hust.repository.course.QuizJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class QuizService {
    private final SectionJpaRepository sectionJpaRepository;
    private final QuizJpaRepository quizJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;

    public QuizResponse getQuizDetails(Long quizId) throws QuizNotFoundException {
        Section section = quizJpaRepository.findById(quizId).get().getSection();
        List<Answer> answers = answerJpaRepository.findByQuizId(quizId);
        List<AnswerResponse> answerResponses = answers.stream().map(
                answer -> AnswerResponse.builder()
                        .id(answer.getId())
                        .content(answer.getContent())
                        .isCorrect(answer.getIsCorrect())
                        .build()
        ).collect(Collectors.toList());
        return quizJpaRepository.findById(quizId).map(
                quiz -> QuizResponse.builder()
                        .id(quiz.getId())
                        .title(quiz.getTitle())
                        .question(quiz.getQuestion())
                        .explanation(quiz.getExplanation())
                        .answers(answerResponses)
                        .sectionId(section.getId())
                        .sectionTitle(section.getTitle())
                        .build()
        ).orElseThrow(() -> new QuizNotFoundException("Quiz with id " + quizId + " could not be found!"));
    }

    @Transactional
    public void saveQuiz(QuizRequest request) throws SectionNotFoundException {
        Optional<Section> sectionOptional = sectionJpaRepository.findById(request.getSectionId());
        if (!sectionOptional.isPresent()) {
            throw new SectionNotFoundException("Section with id " + request.getSectionId() + " could not be found!");
        }
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update quiz
            Quiz quizNeedUpdate = quizJpaRepository.findById(request.getId()).get();
            quizNeedUpdate.setTitle(request.getTitle());
            quizNeedUpdate.setQuestion(request.getQuestion());
            quizNeedUpdate.setExplanation(request.getExplanation());
            answerJpaRepository.deleteByQuizId(quizNeedUpdate.getId());
            List<Answer> answers = request.getAnswers().stream().map(
                    answerRequest -> Answer.builder()
                            .content(answerRequest.getContent())
                            .isCorrect(answerRequest.getIsCorrect())
                            .quiz(quizNeedUpdate)
                            .build()
            ).collect(Collectors.toList());
            answerJpaRepository.saveAll(answers);
            quizJpaRepository.save(quizNeedUpdate);
        } else {
            //create quiz
            Quiz quiz = Quiz.builder()
                    .title(request.getTitle())
                    .question(request.getQuestion())
                    .explanation(request.getExplanation())
                    .section(sectionOptional.get())
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
