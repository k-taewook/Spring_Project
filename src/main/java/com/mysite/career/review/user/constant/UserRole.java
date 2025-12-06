package com.mysite.career.review.user.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MENTOR("ROLE_MENTOR"),
    MENTEE("ROLE_MENTEE");


    private String value;

    UserRole(String value) {
        this.value = value;
    }
}
