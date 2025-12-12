package com.mysite.career.review.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeEducationDto {
    private String schoolName;
    private String major;
    private String startDate;
    private String endDate;
    private String status;
}
