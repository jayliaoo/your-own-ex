package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.math.*;

import static org.example.yourownex.jooq.tables.Account.ACCOUNT;

@Service
public class AccountDao {
    private final DSLContext dslContext;

    public AccountDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public AccountRecord findByUserIdAndCurrency(Long userId, String currency) {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.USER_ID.eq(userId))
                .and(ACCOUNT.CURRENCY.eq(currency))
                .fetchOne();
    }

    public void save(AccountRecord entity) {
        dslContext.insertInto(ACCOUNT).set(entity).execute();
    }

    public void increase(Long id, BigDecimal amount) {
        dslContext.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.add(amount))
                .where(ACCOUNT.ID.eq(id))
                .execute();
    }

    public int decrease(Long id, BigDecimal amount) {
        return dslContext.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.minus(amount))
                .where(ACCOUNT.ID.eq(id))
                .and(ACCOUNT.BALANCE.ge(amount))
                .execute();
    }

}
