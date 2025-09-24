package com.mysite.sbb.answer.entity;

import com.mysite.sbb.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
// @Table(name = "my_table") 테이블 이름 바꾸고 싶을 때
@Getter
@Setter
//@ToString(exclude = "")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)                  // Entity를 생성하면 날짜를 잡아옴
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id; //아이디

    @Column(columnDefinition = "TEXT")
    private String content; // 내용

    @CreatedDate
    private LocalDateTime created; // 작성일

    @ManyToOne(fetch = FetchType.LAZY) // 필요하면 그 때 불러서 매핑 시켜줌 -> 우리는 무조건 이걸로 한다
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
