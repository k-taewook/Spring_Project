package com.mysite.sbb.question.repository;

import com.mysite.sbb.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


// Repository는 DB의 CRUD를 해주는 것
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findBySubjectLike(String keyword);

    Page<Question> findAll(Pageable pageable);

}
