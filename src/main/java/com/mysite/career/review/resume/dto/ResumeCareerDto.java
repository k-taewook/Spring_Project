package com.mysite.career.review.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeCareerDto {
    private String companyName;
    private String department;
    private String position;
    private String startDate;
    private String endDate;
    private String description;
}
