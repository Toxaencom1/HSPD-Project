//package com.taxah.hspd.util;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//@Deprecated
//@Component
//@RequiredArgsConstructor
//public class LocalDateToEpochMillisSerializer extends JsonSerializer<LocalDate> {
//
//    private final DateTimeCustomFormatter formatter;
//
//    @Override
//    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        gen.writeString(value.format(formatter.getFormatter()));
//    }
//}