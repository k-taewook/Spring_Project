package com.mysite.career.review.resume.dto;

import com.mysite.career.review.resume.constant.ResumeStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data                                   // getter, setter, ToString 등등 모든 기능 있음
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDto {

    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요")           // validation 제약
    @NotEmpty(message = "제목은 필수 항목 입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수 항목 입니다.")
    private String content;

    private String targetCompany;

    private ResumeStatus status;

    private MultipartFile resumeFile;
}
