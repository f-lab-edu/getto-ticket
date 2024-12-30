package com.flab.gettoticket.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Object -> JSON
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to JSON 직렬화 중 예외 발생", e);
        }
    }


    // JSON -> Object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON to Object 역직렬화 중 예외 발생", e);
        }
    }

}
