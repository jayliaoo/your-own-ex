package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.*;
import org.jooq.*;
import org.springframework.stereotype.*;

import java.util.*;

import static org.example.yourownex.jooq.Tables.*;


@Service
public class FulfillmentDao {
    private final DSLContext dslContext;

    public FulfillmentDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void save(FulfillmentRecord record) {
        dslContext.executeInsert(record);
    }

    public List<FulfillmentRecord> findToFulfill(int limit) {
        return dslContext.selectFrom(FULFILLMENT)
                .where(FULFILLMENT.STATUS.eq("INIT"))
                .orderBy(FULFILLMENT.ID)
                .limit(limit)
                .fetch();
    }

    public void update(FulfillmentRecord record) {
        dslContext.executeUpdate(record);
    }
}
