package com.scm.order.processing.service;

import com.scm.order.processing.client.ProductClient;
import com.scm.order.processing.client.UserClient;
import com.scm.order.processing.dto.ErrorDTO;
import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import com.scm.order.processing.dto.PurchaseOrderResponseDTO;
import com.scm.order.processing.entity.PurchaseOrder;
import com.scm.order.processing.enums.ErrorType;
import com.scm.order.processing.exception.PurchaseOrderCreateException;
import com.scm.order.processing.helper.PurchaseOrderHelper;
import com.scm.order.processing.mapper.PurchaseOrderDTOMapper;
import com.scm.order.processing.repository.OrderDetailRepository;
import com.scm.order.processing.repository.PurchaseOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PurchaseOrderService {

    private PurchaseOrderRepository purchaseOrderRepository;
    private OrderDetailRepository orderDetailRepository;
    private PurchaseOrderHelper purchaseOrderHelper;
    private PurchaseOrderDTOMapper poMapper;
    private UserClient userClient;
    private ProductClient productClient;

    public PurchaseOrderResponseDTO createOrder(PurchaseOrderDTO orderDTO){
        PurchaseOrder order = null;
        List<ErrorDTO> errorList = new ArrayList<>();
        try{

            Map<Integer, BigDecimal> productCostMap = validateUserAndProduct(orderDTO, errorList);
            if(errorList.isEmpty()){
                purchaseOrderHelper.populateOrderAmount(orderDTO,productCostMap);
                order = poMapper.purchaseOrderDTOToPurchaseOrderEntity(orderDTO);
                purchaseOrderRepository.save(order);
            }
        }catch(Exception e){
            log.error("Error while saving order ",e);
            throw new PurchaseOrderCreateException(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return PurchaseOrderResponseDTO.builder().order(order).errors(errorList).build();
    }

    public String dispatchOrder( Integer productId, Integer orderId){
        return Optional.ofNullable(orderDetailRepository.findByProductIdAndOrderOrderId(productId,orderId))
                .map(detail -> {
                    purchaseOrderRepository.updateDispatchStatus(productId,orderId);
                    return "Order Detail Dispatched Successfully";
                })
                .orElse( "Order Detail Not Found!!");
    }

    private Map<Integer, BigDecimal> validateUserAndProduct(PurchaseOrderDTO orderDTO,List<ErrorDTO> errorList){
        Map<Integer, BigDecimal> productCostMap = new HashMap<>();
        if(userClient.checkUserExists(orderDTO.getOrderedBy())){
            List<Integer> productIdList = orderDTO.getOrderDetails().stream()
                                            .map(OrderDetailDTO::getProductId)
                                            .collect(Collectors.toList());
            productClient.retriveProduct(productIdList).getContent()
            .forEach(prod ->{
                if(NumberUtils.INTEGER_ZERO.equals(prod.getStock())){
                    errorList.add(retrieveErrorDTO("Product Id : " +prod.getProductId()+ "-"
                            +prod.getProductName()+ " out of stock.", "OP-ER02"));
                }
                if("N".equalsIgnoreCase(prod.getAvailability())) {
                    errorList.add(retrieveErrorDTO("Product Id : " +prod.getProductId()+ "-"
                            +prod.getProductName()+ " is coming soon. Not avalaiable yet.", "OP-ER03"));
                }
                productCostMap.put(prod.getProductId(),prod.getCost());
            });
            productIdList.removeAll(productCostMap.keySet());
            if(!productIdList.isEmpty()){
                errorList.add(retrieveErrorDTO("Product Ids : " +productIdList+ " are invalid or Product Service Unavailable", "OP-ER04"));
            }

        }else{
            errorList.add(retrieveErrorDTO("Invalid Customer Id for the Order or User Service Unavailable", "OP-ER01"));
        }
        return productCostMap;
    }

    private ErrorDTO retrieveErrorDTO(String message, String errorCode){
        return ErrorDTO.builder()
                .errorSeverity(ErrorType.ERROR)
                .errorMessage(message)
                .errorCode(errorCode).build();
    }
}
