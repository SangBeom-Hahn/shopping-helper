package com.sketch2fashion.backend.support.upload;

import com.sketch2fashion.backend.support.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String createUuid() {
        return UUID.randomUUID().toString();
    }
}
