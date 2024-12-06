package com.doublegun.travelmgmt.dto;


import lombok.Data;

@Data
public class ApiResponse {

    private String message;
    private Object response;
    private int statusCode;

    public ApiResponse(String message, Object response, int statusCode) {
        this.message = message;
        this.response = response;
        this.statusCode = statusCode;
    }
}
