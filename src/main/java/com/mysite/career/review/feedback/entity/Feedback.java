package com.mysite.career.review.feedback.entity;

import com.mysite.career.review.audit.BaseEntity;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id; //아이디

    @Column(columnDefinition = "TEXT")
    private String content; // 피드백/첨삭 내용

    @Column
    private Integer rating; // 자소서 점수 (1~5)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
}
