package com.taxah.hspd.entity.polygonAPI;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.utils.EpochMillisToLocalDateDeserializer;
import com.taxah.hspd.utils.LocalDateToEpochMillisSerializer;
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
    @JsonIgnore
    private StockResponseData stockResponseData;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_results",
            joinColumns = @JoinColumn(name = "result_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "\nResult{" +
                "id=" + id +
                ", date=" + date +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", srd=" + stockResponseData.getId() +
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

    public void addUser(User user) {
        if (user != null && user.getId() != null) {
            users.add(user);
            user.getResults().add(this);
        }
    }
//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null) return false;
//
//        // Проверка классов с учетом HibernateProxy
//        Class<?> oEffectiveClass = o instanceof HibernateProxy
//                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
//                : o.getClass();
//
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
//                : this.getClass();
//
//        if (!thisEffectiveClass.equals(oEffectiveClass)) return false;
//
//        // Приведение объекта
//        Result result = (Result) o;
//
//        // Сравнение ID, если они существуют
////        if (id != null && result.id != null) {
////            return id.equals(result.id);
////        }
//
//        // Если ID отсутствуют, сравниваем по уникальным полям
//        return Objects.equals(date, result.date) &&
//                Objects.equals(stockResponseData.getTicker(), result.stockResponseData.getTicker());
//    }
//
//    @Override
//    public final int hashCode() {
//        // Учитываем класс с HibernateProxy
//        Class<?> effectiveClass = this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
//                : getClass();
//
//        // Если ID есть, используем его, иначе берем уникальные поля
//        return id != null
//                ? Objects.hash(effectiveClass, id)
//                : Objects.hash(effectiveClass, date, stockResponseData != null ? stockResponseData.getTicker() : null);
//    }
}
