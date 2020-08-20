package com.scm.order.processing.service;


import com.scm.order.processing.dto.ErrorDTO;
import com.scm.order.processing.dto.OrderDetailDTO;
import com.scm.order.processing.dto.PurchaseOrderDTO;
import com.scm.order.processing.dto.PurchaseOrderResponseDTO;
import com.scm.order.processing.entity.OrderDetail;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseOrderServiceTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    MockWebServer userWebServer;
    MockWebServer productWebServer;

    @Before
    public void init() throws IOException {
        userWebServer = new MockWebServer();
        userWebServer.start(8011);
        productWebServer = new MockWebServer();
        productWebServer.start(8021);
    }

    @After
    public void tearDown() throws IOException{
        userWebServer.shutdown();
        productWebServer.shutdown();
    }

    @Test
    @Sql({"/data.sql"})
    public void createOrder_happyflow() throws IOException{
        PurchaseOrderDTO requestDTO = fetchPurchaseOrder();
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        productWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/hal+json")
                .setBody(fetchProductResponse("src/test/resources/products.json")));
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.createOrder(requestDTO);
        Assert.assertNotNull(responseDTO.getOrder().getAmount());
        Assert.assertEquals(0,responseDTO.getErrors().size());
        Assert.assertEquals(BigDecimal.valueOf(7000),responseDTO.getOrder().getAmount());
        for(OrderDetail detail : responseDTO.getOrder().getOrderDetails()){
            Assert.assertNotNull(detail.getAmount());
        }
        Assert.assertEquals(BigDecimal.valueOf(2000),responseDTO.getOrder().getOrderDetails()
                .stream().filter(dtl -> Integer.valueOf(2).equals(dtl.getProductId()))
                .map(OrderDetail::getAmount).findFirst().orElse(BigDecimal.ZERO));
    }

    @Test
    public void createOrder_userServiceDown(){
        PurchaseOrderDTO dto = fetchPurchaseOrder();
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.createOrder(dto);
        Assert.assertNull(responseDTO.getOrder());
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("Invalid Customer Id for the Order or User Service Unavailable",
                responseDTO.getErrors().stream().findFirst().map(ErrorDTO::getErrorMessage).orElse(null));

    }

    @Test
    public void createOrder_productServiceDown(){
        PurchaseOrderDTO dto = fetchPurchaseOrder();
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.createOrder(dto);
        Assert.assertNull(responseDTO.getOrder());
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("OP-ER04",
                responseDTO.getErrors().stream().findFirst().map(ErrorDTO::getErrorCode).orElse(null));

    }

    @Test
    public void createOrder_outOfStockProducts() throws IOException {
        PurchaseOrderDTO dto = fetchPurchaseOrder();
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        productWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/hal+json")
                .setBody(fetchProductResponse("src/test/resources/outofstockproducts.json")));
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.createOrder(dto);
        Assert.assertNull(responseDTO.getOrder());
        Assert.assertEquals(2,responseDTO.getErrors().size());
        Assert.assertEquals(Arrays.asList("OP-ER03","OP-ER02"),
                responseDTO.getErrors().stream().map(ErrorDTO::getErrorCode).collect(Collectors.toList()));

    }

    @Test
    @Sql({"/data.sql"})
    public void dispatchOrder_happyflow(){
        String response = purchaseOrderService.dispatchOrder(4,1);
        Assert.assertNotNull(response);
        Assert.assertEquals("Order Detail Dispatched Successfully",response);
    }

    @Test
    public void dispatchOrder_errorFlow(){
        String response = purchaseOrderService.dispatchOrder(2,1);
        Assert.assertNotNull(response);
        Assert.assertEquals("Order Detail Not Found!!",response);
    }

    private PurchaseOrderDTO fetchPurchaseOrder(){
        OrderDetailDTO detail = OrderDetailDTO.builder().quantity(BigDecimal.ONE).productId(4).build();
        OrderDetailDTO detail2 = OrderDetailDTO.builder().quantity(BigDecimal.TEN).productId(2).build();
        return PurchaseOrderDTO.builder()
                .orderedBy(1)
                .orderDetails(new HashSet<>(Arrays.asList(detail,detail2)))
                .build();
    }

    private String fetchProductResponse(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }

}
