package org.ekgns33.artists.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    @Builder.Default
    private final List<FieldError> errors = new ArrayList<>();

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;
    }
}