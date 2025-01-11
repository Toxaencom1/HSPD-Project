package com.taxah.hspd.entity.polygonAPI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.taxah.hspd.utils.EpochMillisToLocalDateDeserializer;
import com.taxah.hspd.utils.LocalDateToEpochMillisSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("t")
    @JsonDeserialize(using = EpochMillisToLocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateToEpochMillisSerializer.class)
    private LocalDate date;

    @JsonProperty("o")
    private BigDecimal open;

    @JsonProperty("c")
    private BigDecimal close;

    @JsonProperty("h")
    private BigDecimal high;

    @JsonProperty("l")
    private BigDecimal low;

    @ManyToOne
    private StockResponseData stockResponseData;

    @Override
    public String toString() {
        return "\nResult{" +
                "id=" + id +
                ", date=" + date +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                '}';
    }
}
