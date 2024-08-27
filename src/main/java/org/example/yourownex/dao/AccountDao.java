package org.example.yourownex.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.example.yourownex.entity.Account;
import org.springframework.stereotype.Repository;

import static org.example.yourownex.entity.QAccount.account;

@Repository
public class AccountDao {
    private final SQLQueryFactory queryFactory;

    public AccountDao(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Account findByUserIdAndCurrency(Long userId, String currency) {
        return queryFactory.selectFrom(account)
                .from(account)
                .where(account.userId.eq(userId)
                        .and(account.currency.eq(currency)))
                .fetchOne();
    }

    public void save(Account entity) {
        queryFactory.insert(account).populate(entity).execute();
    }
}
