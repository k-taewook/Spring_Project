package com.mysite.career.review.resume.entity;

import com.mysite.career.review.audit.BaseEntity;
import com.mysite.career.review.resume.constant.ResumeStatus;
import com.mysite.career.review.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Column(length = 100)
    private String targetCompany; // 지원 목표 기업

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ResumeStatus status; // 진행 상태 (작성중, 서류제출 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("version DESC")
    private List<Resume> resumes = new ArrayList<>();
}
