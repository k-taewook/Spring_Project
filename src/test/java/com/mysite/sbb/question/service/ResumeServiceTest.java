package com.mysite.sbb.question.service;

import com.mysite.career.review.resume.dto.ResumeDto;
import com.mysite.career.review.resume.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResumeServiceTest {

    @Autowired                                  // 테스트 코드에서는 Autowired로 만들어야 객체가 만들어진다.
    private ResumeService resumeService;

//    @Transactional
    @Test
    void create() {
        for (int i = 1; i < 301; i++) {
            ResumeDto resumeDto = ResumeDto.builder()
                    .subject("질문 제목 " + i)
                    .content("질문 내용 " + i)
                    .build();

        }
    }
}