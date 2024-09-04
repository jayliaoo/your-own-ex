package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.*;
import org.jetbrains.annotations.*;
import org.jooq.*;
import org.springframework.stereotype.*;

import java.time.*;

import static org.example.yourownex.jooq.Tables.*;

@Repository
public class AddressDao {
    private final DSLContext dslContext;

    public AddressDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public AddressRecord findLocked(String currency, Long userId) {
        return dslContext.selectFrom(ADDRESS)
                .where(ADDRESS.CURRENCY.eq(currency))
                .and(ADDRESS.LOCKED.eq(true))
                .and(ADDRESS.LOCKED_BY.eq(userId))
                .and(ADDRESS.LOCKED_UNTIL.ge(LocalDateTime.now()))
                .fetchOne();
    }

    public int lock(String currency, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return dslContext.update(ADDRESS)
                .set(ADDRESS.LOCKED, true)
                .set(ADDRESS.LOCKED_BY, userId)
                .set(ADDRESS.LOCKED_UNTIL, now.plusMinutes(30))
                .where(ADDRESS.CURRENCY.eq(currency))
                .and(ADDRESS.LOCKED.eq(false).or(ADDRESS.LOCKED_UNTIL.lt(now)))
                .execute();
    }

    public void insert(AddressRecord addressRecord) {
        dslContext.executeInsert(addressRecord);
    }

    public void extend(AddressRecord locked) {
        dslContext.executeUpdate(locked.setLockedUntil(LocalDateTime.now().plusMinutes(30)));
    }

    public AddressRecord getByAddress(String address) {
        return dslContext.selectFrom(ADDRESS).where(ADDRESS.ADDRESS_.eq(address)).fetchOne();
    }
}
