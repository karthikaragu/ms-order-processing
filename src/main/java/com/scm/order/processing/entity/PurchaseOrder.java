package com.scm.order.processing.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "purchaseorder", schema="autosparescm")
@Data
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 12845840L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", insertable = false, nullable = false)
    private Integer orderId;

    @NotNull(message = "Enter Invoice Date")
    @Column(name = "invoicedate", nullable = false)
    private LocalDateTime invoiceDate;

    @NotNull(message = "Enter Ordered By !!" )
    @Column(name = "orderedby", nullable = false)
    private Integer orderedBy;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name="orderstatus", referencedColumnName = "statuscode", nullable = false)
    private OrderStatus orderStatus;

}