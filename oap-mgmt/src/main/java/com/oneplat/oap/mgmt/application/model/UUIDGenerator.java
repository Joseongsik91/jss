package com.oneplat.oap.mgmt.application.model;

import static java.util.UUID.randomUUID;

public class UUIDGenerator {

    public String createUUID() {
        return randomUUID().toString();
    }
}
