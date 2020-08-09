package com.scm.order.processing.helper;

import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PurchaseOrderHelper {

    public void populateOrderAmount(PurchaseOrderDTO orderDTO){
        if(CollectionUtils.isNotEmpty(orderDTO.getOrderDetails())){
            orderDTO.getOrderDetails().forEach(detail -> detail.setAmount(BigDecimal.ONE.multiply(detail.getQuantity())));
            orderDTO.setAmount(orderDTO.getOrderDetails().stream()
                    .map(OrderDetailDTO::getAmount)
                    .reduce(BigDecimal.ZERO,BigDecimal::add));
        }
    }
}
