package com.mysite.sbb.question.service;

import com.mysite.resume_clinic.question.dto.QuestionDto;
import com.mysite.resume_clinic.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionServiceTest {

    @Autowired                                  // 테스트 코드에서는 Autowired로 만들어야 객체가 만들어진다.
    private QuestionService questionService;

//    @Transactional
    @Test
    void create() {
        for (int i = 1; i < 301; i++) {
            QuestionDto questionDto = QuestionDto.builder()
                    .subject("질문 제목 " + i)
                    .content("질문 내용 " + i)
                    .build();

        }
    }
}