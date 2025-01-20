package com.taxah.hspd.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.taxah.hspd.util.constant.Constants.INVALID_LOCAL_DATE_TOKEN_DESERIALIZATION;

public class EpochMillisToLocalDateDeserializer extends JsonDeserializer<LocalDate> {


    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        if (p.currentToken().isNumeric()) {
            long timestamp = p.getLongValue();
            return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (p.currentToken().isScalarValue()) {
            String date = p.getText();
            return LocalDate.parse(date);
        } else {
            throw new IOException(INVALID_LOCAL_DATE_TOKEN_DESERIALIZATION);
        }
    }
}