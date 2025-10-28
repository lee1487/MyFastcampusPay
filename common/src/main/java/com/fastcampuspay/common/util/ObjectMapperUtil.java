package com.fastcampuspay.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class ObjectMapperUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // 나노초를 유연하게 처리 (0~9자리 소수점 지원)
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = 
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .optionalStart()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .optionalEnd()
                    .toFormatter();
    
    static {
        // JavaTimeModule 설정 (나노초 유연하게 처리)
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, 
                new LocalDateTimeSerializer(LOCAL_DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, 
                new LocalDateTimeDeserializer(LOCAL_DATE_TIME_FORMATTER));
        
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        objectMapper.registerModule(javaTimeModule);
    }
    
    /**
     * ObjectMapper 인스턴스를 반환합니다.
     * 싱글톤 패턴으로 구현되어 있어 모든 곳에서 같은 인스턴스를 사용합니다.
     * 
     * @return ObjectMapper 인스턴스
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    /**
     * 객체를 JSON 문자열로 변환합니다.
     * 
     * @param obj 변환할 객체
     * @return JSON 문자열
     * @throws RuntimeException 변환 실패 시
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * JSON 문자열을 객체로 변환합니다.
     * 
     * @param json JSON 문자열
     * @param clazz 변환할 클래스
     * @param <T> 변환할 타입
     * @return 변환된 객체
     * @throws RuntimeException 변환 실패 시
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
}

