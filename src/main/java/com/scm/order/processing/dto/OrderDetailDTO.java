package com.scm.order.processing.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Builder
public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 22453472L;

    @NotNull(message = "Enter Product Id !!")
    private Integer productId;

    @NotNull(message = "Enter Quantity")
    private BigDecimal quantity;

    private BigDecimal amount;

    private String statusCode;



}
