package com.bi183.japasuharsa.model;

import java.util.List;

public class ResponseData {

    private String value;
    private String message;
    private List<Buku> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Buku> getResult() {
        return result;
    }
}