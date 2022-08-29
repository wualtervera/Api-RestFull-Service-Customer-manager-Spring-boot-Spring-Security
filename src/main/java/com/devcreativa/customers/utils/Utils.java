package com.devcreativa.customers.utils;

import com.devcreativa.customers.models.errors.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {

    private Utils() {
    }

    public static ResponseEntity responseEntity(Object body, HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).body(body);
    }
    public static Error responseError(String message){
        return new Error(message);
    }
}
