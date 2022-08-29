package com.devcreativa.customers.exceptions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ExceptionResponse{
    private int statusCode;
    private String userMessage;
}
