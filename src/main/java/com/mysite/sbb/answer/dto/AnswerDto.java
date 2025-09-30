package com.mysite.sbb.answer.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                                   // getter, setter, ToString 등등 모든 기능 있음
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    @NotEmpty(message = "내용은 필수 항목 입니다.")
    private String content;
}
