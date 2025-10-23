package com.automationframework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class JsonReader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T read(String classpathJson, Class<T> clazz) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathJson)) {
            return MAPPER.readValue(Objects.requireNonNull(is), clazz);
        } catch (Exception e) {
            LoggerUtil.error("JSON read error: " + classpathJson, e);
            return null;
        }
    }

    public static List<Map<String, Object>> readAsListOfMaps(String classpathJson) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathJson)) {
            return MAPPER.readValue(Objects.requireNonNull(is), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            LoggerUtil.error("JSON read error: " + classpathJson, e);
            return Collections.emptyList();
        }
    }
}
