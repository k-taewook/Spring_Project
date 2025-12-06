package com.mysite.career.review.resume.constant;

import lombok.Getter;

@Getter
public enum ResumeStatus {
    WAITING("요청중"),
    COMPLETED("첨삭완료");

    private final String description;

    ResumeStatus(String description) {
        this.description = description;
    }
}
