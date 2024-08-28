package org.example.yourownex.dao;

import org.example.yourownex.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static org.example.yourownex.jooq.tables.User.USER;


@Service
public class UserDao {
    private final DSLContext dslContext;

    public UserDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public UserRecord findByEmailAndPassword(String email, String password) {
        return dslContext.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .and(USER.PASSWORD.eq(password))
                .fetchOne();
    }

    public Long save(UserRecord user) {
        return dslContext.insertInto(USER).set(user).returning(USER.ID).fetchOne(USER.ID);
    }
}
