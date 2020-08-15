package com.scm.order.processing.client;

import com.scm.order.processing.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "ms-product-management", url = "http://localhost:8021/productmanagementservices")
public interface ProductClient {
    
    @GetMapping(value="/products/search/findByProductIdIn")
    CollectionModel<Product> retriveProduct(@RequestParam("productIds") List<Integer> productIds);

}
