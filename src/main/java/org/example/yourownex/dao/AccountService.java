package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static org.example.yourownex.jooq.tables.Account.ACCOUNT;

@Service
public class AccountService {
    private final DSLContext dslContext;

    public AccountService(DSLContext dslContext) {
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

    public void update(AccountRecord account) {
        dslContext.executeUpdate(account);
    }
}
