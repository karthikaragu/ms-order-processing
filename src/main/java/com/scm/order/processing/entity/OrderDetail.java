package com.scm.order.processing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orderdetail", schema="autosparescm")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1764310L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetailid", insertable = false, nullable = false)
    private Integer orderDetailId;

    @Column(name = "productid", nullable = false)
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="orderdetailstatus", referencedColumnName = "statuscode", nullable = false)
    private OrderStatus orderDetailStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderid",nullable = false)
    @NotNull(message = "Mandatory Field - Order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private PurchaseOrder order;

}