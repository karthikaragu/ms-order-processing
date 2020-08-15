package com.scm.order.processing.repository;

import com.scm.order.processing.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "orderdetails", path = "orderdetails")
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @RestResource(exported = false)
    OrderDetail findByProductIdAndOrderOrderId(Integer productId, Integer orderId);
}