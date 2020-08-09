package com.scm.order.processing.mapper;

import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import com.scm.order.processing.entity.OrderDetail;
import com.scm.order.processing.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PurchaseOrderDTOMapper {

    @Mapping(target = "invoiceDate" ,expression = "java(defaultInvoiceTime())")
    @Mapping(target = "orderStatus.statusCode",constant = "P")
    PurchaseOrder purchaseOrderDTOToPurchaseOrderEntity(PurchaseOrderDTO orderDTO);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "orderDetailStatus.statusCode",constant = "P")
    OrderDetail orderDetailDTOToOrderDetailEntity(OrderDetailDTO orderDetailDTO);

    List<OrderDetail> orderDetailDTOListToOrderDetailListEntity(List<OrderDetailDTO> orderDetailDTOList);

    default LocalDateTime defaultInvoiceTime(){
        return LocalDateTime.now();
    }

}
