package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomResultRepositoryImpl implements CustomResultRepository {
    public static final String DATE = "date";
    public static final String STOCK_RESPONSE_DATA = "stockResponseData";
    public static final String STOCK_RESPONSE_DATA_ID = STOCK_RESPONSE_DATA + "Id";
    public static final String STOCK_RESPONSE_DATA_ID_UNDERSCORE = "stock_response_data_id";
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String CONFLICTS = "conflicts";
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_batch_fetch_size}")
    private int batchSize;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public List<Result> concurrentSaveAll(List<Result> results) {
        List<Result> savedResults = new ArrayList<>();
        List<Result> conflictRows = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            Result result = results.get(i);

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO results (")
                    .append(DATE).append(", ")
                    .append(STOCK_RESPONSE_DATA_ID_UNDERSCORE).append(", ")
                    .append(OPEN).append(", ")
                    .append(CLOSE).append(", ")
                    .append(HIGH).append(", ")
                    .append(LOW).append(") ")
                    .append("VALUES (:").append(DATE).append(", :")
                    .append(STOCK_RESPONSE_DATA_ID).append(", :")
                    .append(OPEN).append(", :")
                    .append(CLOSE).append(", :")
                    .append(HIGH).append(", :")
                    .append(LOW).append(") ")
                    .append("ON CONFLICT (").append(DATE).append(", ")
                    .append(STOCK_RESPONSE_DATA_ID_UNDERSCORE).append(") DO NOTHING RETURNING id;");

            String sql = sqlBuilder.toString();
            /* StringBuilder description:
                    "INSERT INTO results (date, stock_response_data_id, open, close, high, low) " +
                   "VALUES (:date, :stockResponseDataId, :open, :close, :high, :low) " +
                    "ON CONFLICT (date, stock_response_data_id) DO NOTHING RETURNING id";
             */

            Long generatedId = (Long) entityManager.createNativeQuery(sql)
                    .setParameter(DATE, result.getDate())
                    .setParameter(STOCK_RESPONSE_DATA_ID, result.getStockResponseData().getId())
                    .setParameter(OPEN, result.getOpen())
                    .setParameter(CLOSE, result.getClose())
                    .setParameter(HIGH, result.getHigh())
                    .setParameter(LOW, result.getLow())
                    .getSingleResult();

            if (generatedId != null) {
                result.setId(generatedId);
                savedResults.add(result);
            } else {
                conflictRows.add(result);
            }

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        if (!conflictRows.isEmpty()) {
            List<Result> existingResults = entityManager.createQuery(
                            "SELECT r FROM Result r WHERE (r." + DATE + ", r." + STOCK_RESPONSE_DATA + ".id) IN :conflicts", Result.class)
                    .setParameter(CONFLICTS, conflictRows.stream()
                            .map(r -> new AbstractMap.SimpleEntry<>(r.getDate(), r.getStockResponseData().getId()))
                            .collect(Collectors.toList()))
                    .getResultList();

            savedResults.addAll(existingResults);
        }

        entityManager.flush();
        entityManager.clear();

        return savedResults;
    }
}