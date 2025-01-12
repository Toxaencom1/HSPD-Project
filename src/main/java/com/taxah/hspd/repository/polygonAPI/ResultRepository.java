package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.Result;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Transactional
    @Query("SELECT r FROM Result r WHERE r.date BETWEEN :from AND :to AND r.stockResponseData.ticker = :ticker ")
    List<Result> findByDateAndTicker(@Param("ticker") String ticker,
                                     @Param("from") LocalDate dateFrom, @Param("to") LocalDate dateTo);
}
