package org.example.yourownex.service;

import org.example.yourownex.controller.CustomException;
import org.example.yourownex.controller.SignInInterceptor;
import org.example.yourownex.dao.*;
import org.example.yourownex.dto.DepositRequest;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FundsService {
    private final AccountDao accountDao;
    private final StatementDao statementDao;
    private final AddressDao addressDao;
    private final BitcoinService bitcoinService;

    public FundsService(
            AccountDao accountDao, StatementDao statementDao, AddressDao addressDao,
            BitcoinService bitcoinService
    ) {
        this.accountDao = accountDao;
        this.statementDao = statementDao;
        this.addressDao = addressDao;
        this.bitcoinService = bitcoinService;
    }

    @Transactional
    public void deposit(DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, request.getCurrency());
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
        String currency = request.getCurrency();
        AccountRecord account = accountDao.findByUserIdAndCurrency(userId, currency);
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CustomException("Insufficient balance.");
        }
        Long accountId = account.getId();
        if (currency.equals("USD")) {
            statementDao.save(new StatementRecord().setAccountId(accountId)
                    .setAmount(request.getAmount().negate())
                    .setType("WITHDRAW"));
            accountDao.decrease(accountId, request.getAmount());
        } else {//BTC
            bitcoinService.transfer(request.getAddress(), request.getAmount());
            statementDao.save(new StatementRecord().setAccountId(accountId)
                    .setAmount(request.getAmount().negate())
                    .setType("WITHDRAW"));
            accountDao.decrease(accountId, request.getAmount());
        }
    }

    public String depositPrepare(String currency) {
        Long userId = SignInInterceptor.getUserId();
        AddressRecord locked = addressDao.findLocked(currency, userId);
        if (locked == null) {
            int lock = addressDao.lock(currency, userId);
            if (lock < 1) {
                String s = bitcoinService.newAddress();
                addressDao.insert(new AddressRecord().setCurrency(currency).setAddress(s));
                return s;
            } else {
                return addressDao.findLocked(currency, userId).getAddress();
            }
        } else {
            addressDao.extend(locked);
            return locked.getAddress();
        }
    }
}
