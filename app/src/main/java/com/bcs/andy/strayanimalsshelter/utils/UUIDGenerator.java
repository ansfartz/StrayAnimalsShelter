package com.bcs.andy.strayanimalsshelter.utils;

import java.util.UUID;

public class UUIDGenerator {

    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
