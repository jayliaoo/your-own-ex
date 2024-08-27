package org.example.yourownex.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final SQLQueryFactory queryFactory;

    public OrderDao(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
}
