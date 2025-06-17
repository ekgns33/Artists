// src/main/java/org/ekgns33/artists/domain/performance/Genre.java
package org.ekgns33.artists.domain.performance;

import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum Genre {
    PLAY("AAAA", "연극"),
    MUSICAL("BBBC", "뮤지컬"),
    CLASSIC("CCCA", "클래식"),
    OPERA("CCCC", "오페라"),
    CONCERT("EEEB", "콘서트"),
    ETC("ETC", "기타");

    private final String code;
    private final String label;
    public static Genre ofCode(String code){ for(var g:values()) if(g.code.equals(code)) return g; return ETC;}
    public static Genre fromRaw(String raw) {
        if (raw == null || raw.isBlank()) return Genre.ETC;
        String genre = raw.toLowerCase(Locale.ROOT);

        if (genre.matches(".*(뮤지컬).*")) return Genre.MUSICAL;
        if (genre.matches(".*(연극).*")) return Genre.PLAY;
        if (genre.matches(".*(클래식|서양음악|성악|실내악|오케스트라).*")) return Genre.CLASSIC;
        if (genre.matches(".*(오페라).*")) return Genre.OPERA;
        if (genre.matches(".*(콘서트|대중음악|팝|재즈|인디|락|트로트|힙합).*")) return Genre.CONCERT;

        return Genre.ETC;
    }
}
