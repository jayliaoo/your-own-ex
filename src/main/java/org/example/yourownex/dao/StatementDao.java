package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.StatementRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static org.example.yourownex.jooq.tables.Statement.STATEMENT;


@Service
public class StatementDao {
    private final DSLContext dslContext;

    public StatementDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void save(StatementRecord entity) {
        dslContext.insertInto(STATEMENT).set(entity).execute();
    }
}
