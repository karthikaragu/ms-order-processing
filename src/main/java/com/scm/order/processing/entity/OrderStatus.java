package com.scm.order.processing.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "orderstatus", schema="autosparescm")
public class OrderStatus implements Serializable {
    private static final long serialVersionUID = 19457490L;

    @Id
    @Column(name = "statuscode", insertable = false, nullable = false)
    private String statusCode;

    @Column(name = "statusdescription", nullable = false)
    private String statusDescription;

}