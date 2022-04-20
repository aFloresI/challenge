package com.user.registration.challenge.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiErrorDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final HttpStatus status;
    private final String message;

    public ApiErrorDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}