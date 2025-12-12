package com.mysite.career.review.resume.constant;

import lombok.Getter;

@Getter
public enum ResumeVisibility {
    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String description;

    ResumeVisibility(String description) {
        this.description = description;
    }
}
