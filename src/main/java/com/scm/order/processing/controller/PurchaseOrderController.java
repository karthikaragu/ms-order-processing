package com.scm.order.processing.controller;

import com.scm.order.processing.dto.ErrorDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import com.scm.order.processing.dto.PurchaseOrderResponseDTO;
import com.scm.order.processing.enums.ErrorType;
import com.scm.order.processing.exception.PurchaseOrderCreateException;
import com.scm.order.processing.service.PurchaseOrderService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;

    @PostMapping("/createorder")
    public ResponseEntity<PurchaseOrderResponseDTO> createOrder(@RequestBody @Valid PurchaseOrderDTO orderDTO,
                                                                BindingResult result) {
        if(result.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fetchPurchaseOrderResponseDTO(result));
        }
        return Optional.ofNullable(purchaseOrderService.createOrder(orderDTO))
                .map(order -> ResponseEntity.ok().body(order))
                .orElseThrow(() -> new PurchaseOrderCreateException(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Order Creation Failed"));
    }

    private PurchaseOrderResponseDTO fetchPurchaseOrderResponseDTO(BindingResult result){
        List<ErrorDTO> errorList = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
            errorList.add(ErrorDTO.builder()
                    .errorSeverity(ErrorType.FIELD_VALIDATION)
                    .errorMessage(error.getDefaultMessage())
                    .errorCode("OP-FV01").build()));
        return PurchaseOrderResponseDTO.builder()
                .errors(errorList)
                .build();
    }
}
