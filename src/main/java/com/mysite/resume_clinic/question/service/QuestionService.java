package com.mysite.resume_clinic.question.service;

import com.mysite.resume_clinic.answer.entity.Answer;
import com.mysite.resume_clinic.member.entity.Member;
import com.mysite.resume_clinic.question.dto.QuestionDto;
import com.mysite.resume_clinic.question.entity.Question;
import com.mysite.resume_clinic.question.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String keyword) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10,  Sort.by(sorts));

//        Specification<Question> specification = search(keyword);
//        Page<Question> paging = questionRepository.findAll(specification, pageable);

        Page<Question> paging = questionRepository.findAllByKeyword(keyword, pageable);

        return paging;
    }

    private Specification<Question> search(String keyword) {
        return new Specification<Question>() {


            @Override
            public Predicate toPredicate(Root<Question> question, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                Join<Question, Member> member1 = question.join("author", JoinType.LEFT);
                Join<Question, Answer> answer = question.join("answerList", JoinType.LEFT);
                Join<Answer, Member> member2 =  answer.join("author", JoinType.LEFT);

                return criteriaBuilder.or(criteriaBuilder.like(question.get("subject"), "%" + keyword + "%"), // 제목
                        criteriaBuilder.like(question.get("content"), "%" + keyword + "%"),     // 내용
                        criteriaBuilder.like(member1.get("username"), "%" + keyword + "%"),     // 질문 작성자
                        criteriaBuilder.like(member2.get("username"), "%" + keyword + "%"),     // 답변 작성자
                        criteriaBuilder.like(answer.get("content"), "%" + keyword + "%"));      // 답변 내용
            }
        };
    }

    public Question getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 질문 X"));
        return question;
    }

    public void create(QuestionDto questionDto, Member member) {
        Question question = Question.builder()
                .content(questionDto.getContent())
                .subject(questionDto.getSubject())
                .author(member)
                .build();
        questionRepository.save(question);              // insert
    }

    public void modify(Question question, @Valid QuestionDto questionDto) {
        question.setSubject(questionDto.getSubject());
        question.setContent(questionDto.getContent());
        questionRepository.save(question);              // update(question의 값을 가지고 와서 작업하기에 update로 바뀜)
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
