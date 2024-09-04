package org.example.yourownex.service;

import org.example.yourownex.controller.*;
import org.example.yourownex.dao.*;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.math.*;

@Service
public class AccountService {
    private final AccountDao accountDao;
    private final StatementDao statementDao;

    public AccountService(AccountDao accountDao, StatementDao statementDao) {
        this.accountDao = accountDao;
        this.statementDao = statementDao;
    }

    @Transactional
    public void increase(Long userId, String currency, String type, BigDecimal amount) {
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, currency);
        Long accountId = account.getId();
        StatementRecord statement = new StatementRecord();
        statement.setAccountId(accountId);
        statement.setAmount(amount);
        statement.setType(type);
        statementDao.save(statement);
        accountDao.increase(accountId, amount);
    }

    @Transactional
    public void decrease(Long userId, String currency, String type, BigDecimal amount) {
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, currency);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new CustomException("Insufficient balance.");
        }
        Long accountId = account.getId();
        if (accountDao.decrease(accountId, amount) < 1) {
            throw new CustomException("Insufficient balance.");
        }
        statementDao.save(new StatementRecord().setAccountId(accountId)
                .setAmount(amount.negate())
                .setType(type));
    }

}
