package com.mysite.career.review.answer.service;

import com.mysite.career.review.answer.dto.AnswerDto;
import com.mysite.career.review.answer.entity.Answer;
import com.mysite.career.review.answer.repository.AnswerRepository;
import com.mysite.career.review.member.entity.Member;
import com.mysite.career.review.resume.entity.Resume;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Resume resume, AnswerDto answerDto, Member member) {
        Answer answer = Answer.builder()
                .content(answerDto.getContent())
                .resume(resume)
                .author(member)
                .build();

        answerRepository.save(answer);
    }

    public Answer getAnswer(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 답변을 찾을 수 없습니다."));
        return answer;
    }

    public void modify(Answer answer, @Valid AnswerDto answerDto) {
        answer.setContent(answerDto.getContent());
        answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
