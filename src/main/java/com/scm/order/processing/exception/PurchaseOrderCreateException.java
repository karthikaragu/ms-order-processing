package com.scm.order.processing.exception;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderCreateException extends RuntimeException {

    private final int status;

    public PurchaseOrderCreateException(int status, String message){
        super(message);
        this.status = status;
    }

    public PurchaseOrderCreateException(){
        this.status = HttpStatus.SC_NOT_ACCEPTABLE;
    }

    public int getStatus() {
        return status;
    }

}
