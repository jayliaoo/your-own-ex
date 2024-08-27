package org.example.yourownex.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.example.yourownex.entity.Statement;
import org.springframework.stereotype.Repository;

import static org.example.yourownex.entity.QStatement.statement;

@Repository
public class StatementDao {
    private final SQLQueryFactory queryFactory;

    public StatementDao(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Long save(Statement entity) {
        return queryFactory.insert(statement).populate(statement).executeWithKey(statement.id);
    }
}
