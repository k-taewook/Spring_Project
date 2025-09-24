package com.mysite.sbb.question.dto;

import lombok.Builder;
import lombok.Data;

@Data                                   // getter, setter, ToString 등등 모든 기능 있음
@Builder
public class QuestionDto {
    private String subject;
    private String content;
}
