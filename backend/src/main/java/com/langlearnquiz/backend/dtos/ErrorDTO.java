package com.langlearnquiz.backend.dtos;

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
    private HttpStatus statusCode;
    private Date date;

    public ErrorDTO(RuntimeException e, HttpStatus statusCode){
        message = e.getMessage();
        this.statusCode = statusCode;
        date = new Date();
    }
}
