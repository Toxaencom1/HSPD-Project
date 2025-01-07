package com.taxah.hspd.exception.error;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StringError {
    private final UUID errorUUID;
    private String errorMessage;
}
