package com.scm.order.processing.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchaseorder", schema="autosparescm")
@Data
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", insertable = false, nullable = false)
    private Integer orderId;

    @Column(name = "invoicedate", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "orderedby", nullable = false)
    private Integer orderedBy;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "orderstatus")
    private String orderStatus;

    
}