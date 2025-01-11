package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockResponseDataRepository extends JpaRepository<StockResponseData, Long> {
}
