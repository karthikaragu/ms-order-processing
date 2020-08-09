package com.scm.order.processing.dto;

import com.scm.order.processing.entity.PurchaseOrder;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PurchaseOrderResponseDTO implements Serializable {

    private static final long serialVersionUID = 25618819L;

    private PurchaseOrder order;
    private List<ErrorDTO> errors;
}
