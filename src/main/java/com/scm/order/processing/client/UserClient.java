package com.scm.order.processing.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ms-user-management", url = "http://localhost:8011/usermanagementservices")
public interface UserClient {

    @GetMapping("/checkuserexists/{userid}")
    boolean checkUserExists(@PathVariable("userid") Integer userId);
}

