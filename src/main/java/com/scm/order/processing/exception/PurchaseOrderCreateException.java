package com.scm.order.processing.exception;

import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderCreateException extends RuntimeException{

    private String message;

    public PurchaseOrderCreateException(String message){
        this.message = message;
    }

    public PurchaseOrderCreateException(){
        super();
    }


    public String getMessage() {
        return message;
    }
}
