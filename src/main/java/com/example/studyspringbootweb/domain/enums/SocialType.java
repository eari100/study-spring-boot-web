package com.example.studyspringbootweb.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    KAKAO("kakao");

    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }

    public String getValues() {
        return name;
    }

    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);

    }
}
