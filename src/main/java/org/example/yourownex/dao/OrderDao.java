package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.*;
import org.jooq.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

import static org.example.yourownex.jooq.tables.Order.*;

@Service
public class OrderDao {
    private static final List<String> STATUSES = List.of("init", "partially_fulfilled");

    private final DSLContext dslContext;

    public OrderDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void save(OrderRecord orderRecord) {
        dslContext.insertInto(ORDER).set(orderRecord).execute();
    }

    public OrderRecord findBuyOrder() {
        return dslContext.selectFrom(ORDER)
                .where(ORDER.STATUS.in(STATUSES))
                .and(ORDER.TYPE.eq("BUY"))
                .orderBy(ORDER.PRICE.desc(), ORDER.ID)
                .limit(1)
                .fetchOne();
    }

    public List<OrderRecord> findSellOrders(BigDecimal maxPrice) {
        return dslContext.selectFrom(ORDER)
                .where(ORDER.STATUS.in(STATUSES))
                .and(ORDER.TYPE.eq("SELL"))
                .and(ORDER.PRICE.le(maxPrice))
                .orderBy(ORDER.PRICE.desc(), ORDER.ID)
                .limit(10)
                .fetch();
    }

    public void update(OrderRecord record) {
        dslContext.executeUpdate(record);
    }
}
