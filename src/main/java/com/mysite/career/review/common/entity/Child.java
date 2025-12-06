package com.mysite.career.review.common.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Child {
    @Id                                                     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // MySQL - 자동 증가
    @Column(name = "child_id")                             // DB 컬럼명
    private Long id;

    @Column(length = 15)
    private  String name;


    private int age;

    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;
}
