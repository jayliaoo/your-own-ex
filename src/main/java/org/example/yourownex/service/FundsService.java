package org.example.yourownex.service;

import org.example.yourownex.controller.CustomException;
import org.example.yourownex.controller.SignInInterceptor;
import org.example.yourownex.dao.AccountDao;
import org.example.yourownex.dao.StatementDao;
import org.example.yourownex.dto.DepositRequest;
import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.example.yourownex.jooq.tables.records.StatementRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FundsService {
    private final AccountDao accountDao;
    private final StatementDao statementDao;

    public FundsService(AccountDao accountDao, StatementDao statementDao) {
        this.accountDao = accountDao;
        this.statementDao = statementDao;
    }

    @Transactional
    public void deposit(DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, "USD");
        Long accountId = account.getId();
        StatementRecord statement = new StatementRecord();
        statement.setAccountId(accountId);
        statement.setAmount(request.getAmount());
        statement.setType("DEPOSIT");
        statementDao.save(statement);
        accountDao.increase(accountId, request.getAmount());
    }

    @Transactional
    public void withdraw(DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, "USD");
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CustomException("Insufficient balance.");
        }
        Long accountId = account.getId();
        statementDao.save(new StatementRecord().setAccountId(accountId)
                .setAmount(request.getAmount().negate())
                .setType("WITHDRAW"));
        accountDao.decrease(accountId, request.getAmount());
    }
}
