package com.ev.ready.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {

    public static ApiErrorResponse of(
            int status,
            String error,
            String message,
            String path
    ) {
        return new ApiErrorResponse(OffsetDateTime.now(), status, error, message, path, null);
    }

    public static ApiErrorResponse withFieldErrors(
            int status,
            String error,
            String message,
            String path,
            List<FieldError> fieldErrors
    ) {
        return new ApiErrorResponse(OffsetDateTime.now(), status, error, message, path, fieldErrors);
    }

    public record FieldError(
            String field,
            String message
    ) {
    }
}
