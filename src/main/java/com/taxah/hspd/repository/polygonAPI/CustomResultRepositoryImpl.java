package com.taxah.hspd.repository.polygonAPI;

import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.util.SqlConcurrentRequestHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.taxah.hspd.util.constant.Params.*;


@Repository
@RequiredArgsConstructor
public class CustomResultRepositoryImpl implements CustomResultRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_batch_fetch_size}")
    private int batchSize;
    private final SqlConcurrentRequestHandler requestHandler;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public List<Result> concurrentSaveAll(List<Result> results) {
        List<Result> savedResults = new ArrayList<>();
        List<Result> conflictRows = new ArrayList<>();

        /* Sql description .getInsertRequest()

                INSERT INTO results (date, stock_response_data_id, open, close, high, low)
                VALUES (:date, :stockResponseDataId, :open, :close, :high, :low)
                ON CONFLICT (date, stock_response_data_id) DO NOTHING RETURNING id;

             */
        for (int i = 0; i < results.size(); i++) {
            Result result = results.get(i);

            Long generatedId = (Long) entityManager.createNativeQuery(requestHandler.getInsertRequest())
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

        /* Sql description .getSelectiveRequest()

                SELECT r FROM Result r
                WHERE (r.date, r.stockResponseData.id) IN :conflicts"

            */
        if (!conflictRows.isEmpty()) {
            List<Result> existingResults = entityManager.createQuery(
                            requestHandler.getSelectiveRequest(), Result.class)
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
