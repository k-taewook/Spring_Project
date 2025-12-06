package com.mysite.career.review.member.service;

import com.mysite.career.review.member.dto.MemberDto;
import com.mysite.career.review.member.entity.Member;
import com.mysite.career.review.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(@Valid MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword1()))
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                .department(memberDto.getDepartment())
                .registration(memberDto.getRegistration())
                .build();

        memberRepository.save(member);
    }

    public Member getMember(String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다. : " + name));
        return member;
    }
}
