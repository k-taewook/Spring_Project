package com.mysite.career.review.user.entity;

import com.mysite.career.review.audit.BaseTimeEntity;
import com.mysite.career.review.user.constant.Department;
import com.mysite.career.review.user.constant.Gender;
import com.mysite.career.review.user.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Enumerated(EnumType.STRING)
    private UserRole role;        // 권한 (멘토/멘티)

}
