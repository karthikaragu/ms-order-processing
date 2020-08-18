package com.scm.order.processing.helper;

import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseOrderHelperTest {

    @InjectMocks
    private PurchaseOrderHelper purchaseOrderHelper;

    @Test
    public void testPopulateOrderAmount_happyflow(){
        PurchaseOrderDTO dto =  fetchPurchaseOrder();
        purchaseOrderHelper.populateOrderAmount(dto, fetchCostMap());
        Assert.assertNotNull(dto.getAmount());
        Assert.assertEquals(new BigDecimal(20),dto.getAmount());
        for(OrderDetailDTO detail : dto.getOrderDetails()){
            Assert.assertEquals(BigDecimal.TEN,detail.getAmount());
        }
    }

    @Test
    public void testPopulateOrderAmount_emptyDetail(){
        PurchaseOrderDTO dto =  fetchPurchaseOrder();
        dto.setOrderDetails(null);
        purchaseOrderHelper.populateOrderAmount(dto, fetchCostMap());
        Assert.assertNull(dto.getAmount());
    }

    @Test
    public void testPopulateOrderAmount_invalidProduct(){
        PurchaseOrderDTO dto =  fetchPurchaseOrder();
        dto.getOrderDetails().forEach(dtl -> dtl.setProductId(5));
        Assert.assertThrows(RuntimeException.class ,()-> purchaseOrderHelper.populateOrderAmount(dto, fetchCostMap()));
        Assert.assertNull(dto.getAmount());
    }

    private PurchaseOrderDTO fetchPurchaseOrder(){
        OrderDetailDTO detail = OrderDetailDTO.builder().quantity(BigDecimal.ONE).productId(1).build();
        OrderDetailDTO detail2 = OrderDetailDTO.builder().quantity(BigDecimal.TEN).productId(2).build();
        return PurchaseOrderDTO.builder()
                .orderDetails(new HashSet<>(Arrays.asList(detail,detail2)))
                .build();
    }

    private Map<Integer, BigDecimal> fetchCostMap(){
        Map<Integer, BigDecimal> map = new HashMap<>();
        map.put(1,BigDecimal.TEN);
        map.put(2,BigDecimal.ONE);
        return map;
    }
}
