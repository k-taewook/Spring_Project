package com.mysite.resume_clinic.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                                   // getter, setter, ToString 등등 모든 기능 있음
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {

    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요")           // validation 제약
    @NotEmpty(message = "제목은 필수 항목 입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수 항목 입니다.")
    private String content;
}
