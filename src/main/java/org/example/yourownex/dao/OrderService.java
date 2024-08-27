package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.OrderRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.example.yourownex.jooq.tables.Order.ORDER;

@Service
public class OrderService {
    private static final List<String> STATUSES = List.of("init", "partially_fulfilled");

    private final DSLContext dslContext;

    public OrderService(DSLContext dslContext) {
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
                .and(ORDER.TYPE.eq("BUY"))
                .orderBy(ORDER.PRICE.desc(), ORDER.ID)
                .limit(10)
                .fetch();
    }
}
