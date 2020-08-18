package com.scm.order.processing.client;

import com.scm.order.processing.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public CollectionModel<Product> retriveProduct(List<Integer> productIds) {
        log.info("Product Service Down ! Fallback exceuted for products - {}",productIds);
        return CollectionModel.empty();
    }
}
