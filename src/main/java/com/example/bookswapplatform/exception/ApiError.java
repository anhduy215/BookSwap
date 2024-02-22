package com.example.bookswapplatform.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private HttpStatusCode status;
    private String message;
    private List<String> errors;

    //

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
