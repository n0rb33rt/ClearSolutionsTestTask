package com.norbert.clearsolutions.exception;

import lombok.Builder;

@Builder
public record ApiException(
        String error,
        String message,
        String path
) {
}
