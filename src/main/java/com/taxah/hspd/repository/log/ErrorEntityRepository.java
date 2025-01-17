package com.taxah.hspd.repository.log;

import com.taxah.hspd.entity.log.ErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ErrorEntityRepository extends JpaRepository<ErrorEntity, UUID> {
}
