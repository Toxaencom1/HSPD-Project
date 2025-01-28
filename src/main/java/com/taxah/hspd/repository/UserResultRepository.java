package com.taxah.hspd.repository;

import com.taxah.hspd.entity.UserResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserResultRepository extends JpaRepository<UserResult, Long>, CustomUserResultRepository {
    List<UserResult> findByUserIdAndResultStockResponseDataId(Long userId, Long ticker);
}
