package com.mysite.sbb.common.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor      // 기본 생성자
@AllArgsConstructor     // 전체 생성자
@Builder
public class Parent {
    @Id                                                     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // MySQL - 자동 증가
    @Column(name = "parent_id")                             // DB 컬럼명
    private Long id;                                        // 변수명

    @Column(length = 15)
    private  String name;


    private int age;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)          // mappedBy -> 어디를 바라보고 매핑할거냐? ->
    private List<Child> childList;                                                              // Child에서 foreign key로 parent라고 했기에 parent라고 mappedBy에 적는다
}

