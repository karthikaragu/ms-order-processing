package com.scm.order.processing.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orderdetail", schema="autosparescm")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetailid", insertable = false, nullable = false)
    private Integer orderDetailId;

    @Column(name = "orderid", nullable = false)
    private Integer orderId;

    @Column(name = "productid", nullable = false)
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "orderdetailstatus")
    private String orderDetailStatus;

    
}