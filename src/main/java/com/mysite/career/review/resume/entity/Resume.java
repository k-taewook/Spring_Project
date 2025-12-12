package com.mysite.career.review.resume.entity;

import com.mysite.career.review.audit.BaseEntity;
import com.mysite.career.review.feedback.entity.Feedback;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.resume.constant.ResumeStatus;
import com.mysite.career.review.resume.constant.ResumeVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private String content; // 자소서 본문 (Deprecated: selfIntroList로 대체)

    @ToString.Exclude
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResumeSelfIntro> selfIntroList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResumeCareer> careerList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResumeEducation> educationList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResumeProject> projectList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResumeSkill> skillList = new ArrayList<>();

    @Column(length = 100)
    private String targetCompany; // 지원 목표 기업 (Deprecated: Application으로 이동 예정)

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    @Column
    private Integer version;

    @Column(length = 200)
    private String commitMessage;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ResumeStatus status; // 진행 상태

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ResumeVisibility visibility = ResumeVisibility.PUBLIC;

    public ResumeVisibility getVisibility() {
        return visibility == null ? ResumeVisibility.PUBLIC : visibility;
    }

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Feedback> feedbackList; // 피드백 리스트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User author;
}
