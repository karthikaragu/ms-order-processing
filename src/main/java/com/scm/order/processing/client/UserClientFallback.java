package com.scm.order.processing.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallback implements UserClient {
    @Override
    public boolean checkUserExists(Integer userId) {
        log.info("User Service Down ! Fallback exceuted for userId - {}",userId);
        return false;
    }
}
