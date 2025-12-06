package com.mysite.resume_clinic.member.repository;

import com.mysite.resume_clinic.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String username);

    Optional<Member> findByUsername(String username);
}
