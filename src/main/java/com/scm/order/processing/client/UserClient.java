package com.scm.order.processing.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-user-management", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/usermanagementservices/checkuserexists/{userid}")
    boolean checkUserExists(@PathVariable("userid") Integer userId);

}

