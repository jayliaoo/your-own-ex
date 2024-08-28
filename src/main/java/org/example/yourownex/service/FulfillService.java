package org.example.yourownex.service;

import org.example.yourownex.dao.*;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.*;

@Service
public class FulfillService {
    private final AccountDao accountDao;
    private final StatementDao statementDao;
    private final FulfillmentDao fulfillmentDao;

    public FulfillService(AccountDao accountDao, StatementDao statementDao, FulfillmentDao fulfillmentDao) {
        this.accountDao = accountDao;
        this.statementDao = statementDao;
        this.fulfillmentDao = fulfillmentDao;
    }

    public List<FulfillmentRecord> getToFulfill() {
        return fulfillmentDao.findToFulfill(100);
    }

    @Transactional
    public void fulfill(FulfillmentRecord record) {
        // increase buyer's crypto account balance
        AccountRecord account = accountDao.findByUserIdAndCurrency(record.getBuyerId(),
                record.getCurrency());
        accountDao.increase(account.getId(), record.getAmount());
        statementDao.save(new StatementRecord().setAccountId(account.getId())
                .setType("BUY")
                .setAmount(record.getAmount()));
        // increase seller's USD account balance
        AccountRecord account2 = accountDao.findByUserIdAndCurrency(record.getSellerId(), "USD");
        BigDecimal amount = record.getAmount().multiply(record.getPrice());
        accountDao.increase(account2.getId(), amount);
        statementDao.save(new StatementRecord().setAccountId(account2.getId())
                .setType("SELL")
                .setAmount(amount));
        record.setStatus("DONE");
        fulfillmentDao.update(record);
    }
}
