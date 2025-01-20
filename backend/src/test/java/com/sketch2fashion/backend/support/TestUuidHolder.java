package com.sketch2fashion.backend.support;

import org.springframework.stereotype.Component;

@Component
public class TestUuidHolder implements UuidHolder {

    @Override
    public String createUuid() {
        return "ABCD1234";
    }
}
