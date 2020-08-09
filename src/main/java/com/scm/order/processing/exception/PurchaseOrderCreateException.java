package com.scm.order.processing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderCreateException extends RuntimeException{

    private final HttpStatus status;

    public PurchaseOrderCreateException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
