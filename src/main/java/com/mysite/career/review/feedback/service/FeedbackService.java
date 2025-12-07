package com.mysite.career.review.feedback.service;

import com.mysite.career.review.feedback.dto.FeedbackDto;
import com.mysite.career.review.feedback.entity.Feedback;
import com.mysite.career.review.feedback.repository.FeedbackRepository;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.entity.Resume;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public void create(Resume resume, FeedbackDto feedbackDto, User user) {
        Feedback feedback = Feedback.builder()
                .content(feedbackDto.getContent())
                .resume(resume)
                .author(user)
                .build();

        feedbackRepository.save(feedback);
    }

    public Feedback getFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 피드백을 찾을 수 없습니다."));
        return feedback;
    }

    public void modify(Feedback feedback, @Valid FeedbackDto feedbackDto) {
        feedback.setContent(feedbackDto.getContent());
        feedbackRepository.save(feedback);
    }

    public void delete(Feedback feedback) {
        feedbackRepository.delete(feedback);
    }
}
