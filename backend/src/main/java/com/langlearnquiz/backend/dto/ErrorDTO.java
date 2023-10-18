package com.langlearnquiz.backend.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String message;
    private int statusCode;
    private Date date;

    public ErrorDTO(Exception e, HttpStatus statusCode){
        message = e.getMessage();
        this.statusCode = statusCode.value();
        date = new Date();
    }

    public ErrorDTO(String reason, HttpStatus statusCode){
        message = reason;
        this.statusCode = statusCode.value();
        date = new Date();
    }
}
