package com.example.membership_api.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import com.example.membership_api.model.Subscription;

public class SubscriptionUtils {

    public static String generateSubscriptionNumber() {
        return "SUB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static void populateFields(Subscription subscription, Map<String, Object> data) {
        data.forEach((key, value) -> setField(subscription, key, value));
    }

    public static void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
        }
    }
}
