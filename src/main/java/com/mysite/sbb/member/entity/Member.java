package com.mysite.sbb.member.entity;

import com.mysite.sbb.audit.BaseEntity;
import com.mysite.sbb.audit.BaseTimeEntity;
import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.constant.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

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
