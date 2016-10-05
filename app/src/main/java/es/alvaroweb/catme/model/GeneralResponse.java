/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import static android.R.id.message;

/**
 *
 * this class performs as an xml file like:
 *
 * <response>
    <data />
     <apierror>
        <message>message content.</message>
     </apierror>
 </response>
 */
@Root(name = "response")
public class GeneralResponse {
    @Element
    public String data;

    @Element
    private ApiError apiError;

    private static class ApiError {
        @Element
        String message;
    }


    public String getApiErrorMessage() {
        String message = apiError.message;
        if(message == null){
            return "no error";
        }else{
            return message;
        }
    }
}
