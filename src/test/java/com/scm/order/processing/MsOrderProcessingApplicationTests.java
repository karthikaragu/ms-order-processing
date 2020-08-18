package com.scm.order.processing;

import com.scm.order.processing.controller.PurchaseOrderController;
import com.scm.order.processing.repository.OrderDetailRepository;
import com.scm.order.processing.repository.PurchaseOrderRepository;
import com.scm.order.processing.service.PurchaseOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MsOrderProcessingApplicationTests {

	@Autowired
	private PurchaseOrderController purchaseOrderController;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Test
	void contextLoads() {
		Assert.assertNotNull(purchaseOrderController);
		Assert.assertNotNull(purchaseOrderService);
		Assert.assertNotNull(purchaseOrderRepository);
		Assert.assertNotNull(orderDetailRepository);
	}

}
