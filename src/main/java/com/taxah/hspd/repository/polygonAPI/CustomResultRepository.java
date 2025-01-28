package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.Result;

import java.util.List;

public interface CustomResultRepository {
    List<Result> concurrentSaveAll(List<Result> results);
}
