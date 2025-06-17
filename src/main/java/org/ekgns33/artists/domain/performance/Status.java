// src/main/java/org/ekgns33/artists/domain/performance/Status.java
package org.ekgns33.artists.domain.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum Status {
    SCHEDULED("01","공연예정"),
    ONGOING  ("02","공연중"),
    CLOSED   ("03","공연완료"),
    UNKNOWN  ("","미확인");

    private final String code;
    private final String label;
    public static Status of(String raw){
        for(var s:values()) if(s.code.equals(raw)||s.label.equals(raw)) return s;
        return UNKNOWN;
    }
}
