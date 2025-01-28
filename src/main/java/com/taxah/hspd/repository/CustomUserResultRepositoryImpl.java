package com.taxah.hspd.repository;

import com.taxah.hspd.entity.UserResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomUserResultRepositoryImpl implements CustomUserResultRepository {
    public static final String USER_ID = "userId";
    public static final String RESULT_ID = "resultId";
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_batch_fetch_size}")
    private int batchSize;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void insertUserResults(List<UserResult> userResults) {
        String sql = "INSERT INTO users_results (user_id, result_id) VALUES (:"+USER_ID+", :"+RESULT_ID+")";

        for (int i = 0; i < userResults.size(); i++) {
            UserResult userResult = userResults.get(i);

            entityManager.createNativeQuery(sql)
                    .setParameter(USER_ID, userResult.getUser().getId())
                    .setParameter(RESULT_ID, userResult.getResult().getId())
                    .executeUpdate();

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
