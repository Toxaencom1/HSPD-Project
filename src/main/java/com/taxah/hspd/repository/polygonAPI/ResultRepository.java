package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @EntityGraph(attributePaths = {"stockResponseData"})
    @Query("SELECT r FROM Result r WHERE r.date BETWEEN :from AND :to AND r.stockResponseData.ticker = :ticker ")
    List<Result> findByDateAndTicker(@Param("ticker") String ticker,
                                     @Param("from") LocalDate dateFrom, @Param("to") LocalDate dateTo);


    @EntityGraph(attributePaths = {"stockResponseData", "users"})
    @Query("SELECT r FROM Result r JOIN r.users u JOIN r.stockResponseData srd WHERE srd.ticker = :ticker AND u.id= :id")
    List<Result> findResultsByUserAndTicker(@Param("id") Long id, @Param("ticker") String ticker);
}
