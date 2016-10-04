/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * TODO: Create JavaDoc
 */
@Root(name = "response")
public class GeneralResponse {
//    @Element
//    private String data;

    @Element
    private ApiError apiError;

    private static class ApiError {
        @Element
        String message;
    }


    public String getApiErrorMessage() {
        return apiError.message;
    }
}
