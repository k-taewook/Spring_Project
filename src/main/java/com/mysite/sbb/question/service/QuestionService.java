package com.mysite.sbb.question.service;

import com.mysite.sbb.question.dto.QuestionDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 질문 X"));
        return question;
    }

    public void create(QuestionDto questionDto) {
        Question question = Question.builder()
                .content(questionDto.getContent())
                .subject(questionDto.getSubject())
                .build();
        questionRepository.save(question);
    }
}
