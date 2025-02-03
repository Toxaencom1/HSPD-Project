package com.taxah.hspd.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

import static com.taxah.hspd.util.constant.Params.*;

@Data
@Component
public class SqlConcurrentRequestHandler {
    private String insertRequest;
    private String selectiveRequest;

    @PostConstruct
    public void init() {
        this.insertRequest = String.format("""
                        INSERT INTO results (%s, %s, %s, %s, %s, %s)
                        VALUES (:%s, :%s, :%s, :%s, :%s, :%s)
                        ON CONFLICT (%s, %s) DO NOTHING RETURNING id;
                        """,
                DATE, STOCK_RESPONSE_DATA_ID_UNDERSCORE, OPEN, CLOSE, HIGH, LOW,
                DATE, STOCK_RESPONSE_DATA_ID, OPEN, CLOSE, HIGH, LOW,
                DATE, STOCK_RESPONSE_DATA_ID_UNDERSCORE
        );

        this.selectiveRequest = String.format("""
                SELECT r FROM Result r
                WHERE (r.%s, r.%s.id) IN :%s"
                """, DATE, STOCK_RESPONSE_DATA, CONFLICTS
        );
    }
}
