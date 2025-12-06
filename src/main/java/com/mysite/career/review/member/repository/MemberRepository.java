package com.mysite.career.review.member.repository;

import com.mysite.career.review.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String username);

    Optional<Member> findByUsername(String username);
}
