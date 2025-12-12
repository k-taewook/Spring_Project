package com.mysite.career.review.resume.repository;

import com.mysite.career.review.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


// Repository는 DB의 CRUD를 해주는 것
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findBySubjectLike(String keyword);

    Page<Resume> findAll(Pageable pageable);

    Page<Resume> findAll(Specification<Resume> specification, Pageable pageable);

    @Query(value =
            "SELECT distinct q "
            + "FROM Resume q "
            + "LEFT OUTER JOIN User m1 ON q.author = m1 "
            + "LEFT OUTER JOIN Feedback f ON f.resume = q "
            + "LEFT OUTER JOIN User m2 ON f.author = m2 "
            + "WHERE (q.subject LIKE %:keyword% "
            + "OR q.content LIKE %:keyword% "
            + "OR m1.username LIKE %:keyword% "
            + "OR m2.username LIKE %:keyword% "
            + "OR f.content LIKE %:keyword%) "
            + "AND q.version = (SELECT MAX(r2.version) FROM Resume r2 WHERE r2.application = q.application)"
            , nativeQuery = false)
    Page<Resume> findAllByKeyword(String keyword, Pageable pageable);
}
