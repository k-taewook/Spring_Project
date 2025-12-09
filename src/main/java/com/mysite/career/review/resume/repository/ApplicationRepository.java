package com.mysite.career.review.resume.repository;

import com.mysite.career.review.resume.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
