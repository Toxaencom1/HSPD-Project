package com.taxah.hspd.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@Deprecated
public class LocalDateToEpochMillisSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        long timestamp = value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        gen.writeNumber(timestamp);
    }
}