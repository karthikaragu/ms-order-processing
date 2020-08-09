package com.scm.order.processing.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PurchaseOrderDTO implements Serializable {

    private static final long serialVersionUID = 24672579L;

    private LocalDateTime invoiceDate;

    @NotNull(message = "Enter Ordered By !!")
    private Integer orderedBy;

    private BigDecimal amount;

    @NotEmpty(message = "Order should have atleast one product !!")
    private List<@Valid OrderDetailDTO> orderDetails;

    private String statusCode;
}
