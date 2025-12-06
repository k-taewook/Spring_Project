package com.mysite.resume_clinic.member.entity;

import com.mysite.resume_clinic.audit.BaseTimeEntity;
import com.mysite.resume_clinic.member.constant.Department;
import com.mysite.resume_clinic.member.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;        // 사용자 id

    @Column(nullable = false)
    private String password;        // 비밀번호

    @Column(unique = true, nullable = false)
    private String email;           // 이메일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;          // 성별

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Department department;      // 학과

    @Column(nullable = false)
    private Boolean registration;   // 등록 여부

}
