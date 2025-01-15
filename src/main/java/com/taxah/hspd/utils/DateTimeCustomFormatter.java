package com.taxah.hspd.utils;

import com.taxah.hspd.utils.constant.Constants;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Getter
public class DateTimeCustomFormatter {
    private DateTimeFormatter formatter;

    public String format(LocalDate date) {
        return date.format(formatter);
    }

    @PostConstruct
    public void setFormatter() {
        this.formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    }
}
