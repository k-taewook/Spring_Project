package com.mysite.career.review.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeProjectDto {
    private String projectName;
    private String startDate;
    private String endDate;
    private String description;
    private String gitUrl;
    private String demoUrl;
}
