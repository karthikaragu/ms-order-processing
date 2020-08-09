package com.scm.order.processing.service;

import com.scm.order.processing.dto.PurchaseOrderDTO;
import com.scm.order.processing.dto.PurchaseOrderResponseDTO;
import com.scm.order.processing.entity.PurchaseOrder;
import com.scm.order.processing.exception.PurchaseOrderCreateException;
import com.scm.order.processing.helper.PurchaseOrderHelper;
import com.scm.order.processing.mapper.PurchaseOrderDTOMapper;
import com.scm.order.processing.repository.PurchaseOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PurchaseOrderService {

    private PurchaseOrderRepository purchaseOrderRepository;
    private PurchaseOrderHelper purchaseOrderHelper;
    private PurchaseOrderDTOMapper poMapper;

    public PurchaseOrderResponseDTO createOrder(PurchaseOrderDTO orderDTO) throws PurchaseOrderCreateException{
        PurchaseOrder order = null;
        purchaseOrderHelper.populateOrderAmount(orderDTO);
        try{
            order = poMapper.purchaseOrderDTOToPurchaseOrderEntity(orderDTO);
            purchaseOrderRepository.save(order);
        }catch(Exception e){
            log.error("Error while saving order ",e);
            throw new PurchaseOrderCreateException(e.getMessage());
        }
        return PurchaseOrderResponseDTO.builder()
                .order(order).build();
    }

}
