package com.mysite.sbb.answer.entity;

import com.mysite.sbb.audit.BaseEntity;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
// @Table(name = "my_table") 테이블 이름 바꾸고 싶을 때
@Getter
@Setter
//@ToString(exclude = "")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id; //아이디

    @Column(columnDefinition = "TEXT")
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY) // 필요하면 그 때 불러서 매핑 시켜줌 -> 우리는 무조건 이걸로 한다
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;
}
