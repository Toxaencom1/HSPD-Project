package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockResponseDataRepository extends JpaRepository<StockResponseData, Long> {
    Optional<StockResponseData> findByTicker(String ticker);
}
