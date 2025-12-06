package com.mysite.career.review.resume.service;

import com.mysite.career.review.answer.entity.Answer;
import com.mysite.career.review.member.entity.Member;
import com.mysite.career.review.resume.dto.ResumeDto;
import com.mysite.career.review.resume.entity.Resume;
import com.mysite.career.review.resume.repository.ResumeRepository;
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
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Page<Resume> getList(int page, String keyword) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10,  Sort.by(sorts));

//        Specification<Question> specification = search(keyword);
//        Page<Question> paging = questionRepository.findAll(specification, pageable);

        Page<Resume> paging = resumeRepository.findAllByKeyword(keyword, pageable);

        return paging;
    }

    private Specification<Resume> search(String keyword) {
        return new Specification<Resume>() {


            @Override
            public Predicate toPredicate(Root<Resume> question, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                Join<Resume, Member> member1 = question.join("author", JoinType.LEFT);
                Join<Resume, Answer> answer = question.join("answerList", JoinType.LEFT);
                Join<Answer, Member> member2 =  answer.join("author", JoinType.LEFT);

                return criteriaBuilder.or(criteriaBuilder.like(question.get("subject"), "%" + keyword + "%"), // 제목
                        criteriaBuilder.like(question.get("content"), "%" + keyword + "%"),     // 내용
                        criteriaBuilder.like(member1.get("username"), "%" + keyword + "%"),     // 질문 작성자
                        criteriaBuilder.like(member2.get("username"), "%" + keyword + "%"),     // 답변 작성자
                        criteriaBuilder.like(answer.get("content"), "%" + keyword + "%"));      // 답변 내용
            }
        };
    }

    public Resume getQuestion(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 질문 X"));
        return resume;
    }

    public void create(ResumeDto resumeDto, Member member) {
        Resume resume = Resume.builder()
                .content(resumeDto.getContent())
                .subject(resumeDto.getSubject())
                .author(member)
                .build();
        resumeRepository.save(resume);              // insert
    }

    public void modify(Resume resume, @Valid ResumeDto resumeDto) {
        resume.setSubject(resumeDto.getSubject());
        resume.setContent(resumeDto.getContent());
        resumeRepository.save(resume);              // update(question의 값을 가지고 와서 작업하기에 update로 바뀜)
    }

    public void delete(Resume resume) {
        resumeRepository.delete(resume);
    }
}
