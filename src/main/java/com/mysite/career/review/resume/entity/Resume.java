package com.mysite.career.review.resume.entity;

import com.mysite.career.review.answer.entity.Answer;
import com.mysite.career.review.audit.BaseEntity;
import com.mysite.career.review.member.entity.Member;
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
    @Column(name = "question_id")
    private Long id; //아이디

    @Column(length = 200, nullable = false)
    private String subject; // 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 질문내용

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList; // 보이진 않지만 answer를 보기 위해 answer을 불러올 때 List 타입으로 가져온다. 라는 의미

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",  nullable = false)
    private Member author;
}
