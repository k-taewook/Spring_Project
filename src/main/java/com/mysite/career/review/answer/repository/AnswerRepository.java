package com.mysite.career.review.answer.repository;

import com.mysite.career.review.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
