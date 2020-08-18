package com.scm.order.processing.client;

import com.scm.order.processing.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "ms-product-management", fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping(value="/productmanagementservices/products/search/findByProductIdIn")
    CollectionModel<Product> retriveProduct(@RequestParam("productIds") List<Integer> productIds);

}
