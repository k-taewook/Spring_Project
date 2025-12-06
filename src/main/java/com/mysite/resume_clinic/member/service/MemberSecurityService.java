package com.mysite.resume_clinic.member.service;

import com.mysite.resume_clinic.member.constant.MemberRole;
import com.mysite.resume_clinic.member.entity.Member;
import com.mysite.resume_clinic.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSecurityService implements UserDetailsService {


    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 사용자는 존재하지 않습니다" + username));

        log.info("=============================== 사용자 정보" + member);

        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }


        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
