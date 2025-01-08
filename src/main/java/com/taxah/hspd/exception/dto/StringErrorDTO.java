package com.taxah.hspd.exception.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StringErrorDTO {
    private final UUID errorUUID;
    private String errorMessage;
}
