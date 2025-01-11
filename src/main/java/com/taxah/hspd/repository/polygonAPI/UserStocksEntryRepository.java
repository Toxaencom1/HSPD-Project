package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.UserStocksEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStocksEntryRepository extends JpaRepository<UserStocksEntry, Long> {
}
