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
    private int statusCode;
    private Date date;

    public ErrorDTO(Exception e, HttpStatus statusCode){
        message = e.getMessage();
        this.statusCode = statusCode.value();
        date = new Date();
    }
}
