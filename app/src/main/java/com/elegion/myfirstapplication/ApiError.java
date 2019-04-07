package com.elegion.myfirstapplication;

/**
 * Created by andrew on 07.04.2019.
 */

public class ApiError {
    private int statusCode;
    private String endpoint;
    private String message;

    public int getStatusCode() { return statusCode; }
    public String getEndPoint(){ return endpoint;   }
    public String getMessage() { return message;    }
}
