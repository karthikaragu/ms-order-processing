package com.scm.order.processing.repository;

import com.scm.order.processing.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.transaction.Transactional;

@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    @RestResource(exported = false)
    @Transactional
    @Modifying
    @Query(value = "update autosparescm.orderdetail set orderdetailstatus = 'D' " +
            "where productid = ? and orderid = ?", nativeQuery = true)
    void updateDispatchStatus(@Param("productId") Integer productId, @Param("orderId")Integer orderId);
}