package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.*;
import org.jooq.*;
import org.springframework.stereotype.*;


@Service
public class StatementDao {
    private final DSLContext dslContext;

    public StatementDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void save(StatementRecord entity) {
        dslContext.executeInsert(entity);
    }
}
