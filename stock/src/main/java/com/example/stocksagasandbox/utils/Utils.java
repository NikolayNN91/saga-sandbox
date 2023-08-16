package com.example.stocksagasandbox.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    private Utils() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String writeValueAsString(T toJson) {
        try {
            return MAPPER.writeValueAsString(toJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
