package com.mysite.career.review.resume.entity;

import com.mysite.career.review.audit.BaseEntity;
import com.mysite.career.review.feedback.entity.Feedback;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.constant.ResumeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id; //아이디

    @Column(length = 200, nullable = false)
    private String subject; // 지원 분야/제목

    @Column(columnDefinition = "TEXT")
    private String content; // 자소서 본문

    @Column(length = 100)
    private String targetCompany; // 지원 목표 기업

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ResumeStatus status; // 진행 상태

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Feedback> feedbackList; // 피드백 리스트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User author;
}
