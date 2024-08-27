package org.example.yourownex.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.example.yourownex.entity.User;
import org.springframework.stereotype.Repository;

import static org.example.yourownex.entity.QUser.user;

@Repository
public class UserDao {
    private final SQLQueryFactory queryFactory;

    public UserDao(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public User findByEmailAndPassword(String email, String password) {
        return queryFactory.selectFrom(user)
                .where(user.email.eq(email).
                        and(user.password.eq(password)))
                .fetchOne();
    }
}
