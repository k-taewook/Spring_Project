package com.mysite.career.review.feedback.repository;

import com.mysite.career.review.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
