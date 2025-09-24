package com.mysite.sbb.question.entity;

import com.mysite.sbb.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)                  // Entity를 생성하면 날짜를 잡아옴
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id; //아이디

    @Column(length = 200, nullable = false)
    private String subject; // 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 질문내용

    @CreatedDate
    private LocalDateTime created; // 질문 생성일

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList; // 보이진 않지만 answer를 보기 위해 answer을 불러올 때 List 타입으로 가져온다. 라는 의미
}
