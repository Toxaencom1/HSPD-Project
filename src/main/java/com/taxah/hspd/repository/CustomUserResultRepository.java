package com.taxah.hspd.repository;

import com.taxah.hspd.entity.UserResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserResultRepository {
    void insertUserResults(List<UserResult> userResults);
}
