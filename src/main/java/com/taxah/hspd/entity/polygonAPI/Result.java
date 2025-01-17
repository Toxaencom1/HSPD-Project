package com.taxah.hspd.entity.polygonAPI;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.util.EpochMillisToLocalDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "results", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "stock_response_data_id"})
})
@Schema(description = "Сущность результата")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("t")
    @JsonDeserialize(using = EpochMillisToLocalDateDeserializer.class)
    @Schema(name = "date", description = "Дата акции", example = "2025-01-01")
    private LocalDate date;

    @JsonProperty("o")
    @Schema(name = "open", description = "Цена открытия", example = "134.83")
    private BigDecimal open;

    @JsonProperty("c")
    @Schema(name = "close", description = "Цена закрытия", example = "134.83")
    private BigDecimal close;

    @JsonProperty("h")
    @Schema(name = "high", description = "Максимальная цена", example = "134.83")
    private BigDecimal high;

    @JsonProperty("l")
    @Schema(name = "low", description = "Минимальная цена", example = "134.83")
    private BigDecimal low;

    @ManyToOne
    @JsonIgnore
    private StockResponseData stockResponseData;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "users_results",
            joinColumns = @JoinColumn(name = "result_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (user != null && user.getId() != null) {
            users.add(user);
            user.getResults().add(this);
        }
    }

    @Override
    public String toString() {
        return "\nResult{" +
                "id=" + id +
                ", date=" + date +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
//                ", srd=" + stockResponseData.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(date, result.date) &&
                Objects.equals(stockResponseData.getTicker(), result.stockResponseData.getTicker());
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, stockResponseData != null ? stockResponseData.getTicker() : null);
    }
}
