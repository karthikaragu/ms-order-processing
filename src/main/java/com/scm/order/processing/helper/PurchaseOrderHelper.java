package com.scm.order.processing.helper;

import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class PurchaseOrderHelper {

    public void populateOrderAmount(PurchaseOrderDTO orderDTO, Map<Integer, BigDecimal> productCostMap){
        if(CollectionUtils.isNotEmpty(orderDTO.getOrderDetails())){
            orderDTO.getOrderDetails().forEach(detail ->
                    detail.setAmount(productCostMap.get(detail.getProductId()).multiply(detail.getQuantity())));
            orderDTO.setAmount(orderDTO.getOrderDetails().stream()
                    .map(OrderDetailDTO::getAmount)
                    .reduce(BigDecimal.ZERO,BigDecimal::add));
        }
    }
}
