package org.example.yourownex.service;

import org.example.yourownex.controller.*;
import org.example.yourownex.dao.*;
import org.example.yourownex.dto.*;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.*;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final AccountDao accountDao;
    private final StatementDao statementDao;
    private final FulfillmentDao fulfillmentDao;

    public OrderService(
            OrderDao orderDao, AccountDao accountDao, StatementDao statementDao,
            FulfillmentDao fulfillmentDao
    ) {
        this.orderDao = orderDao;
        this.accountDao = accountDao;
        this.statementDao = statementDao;
        this.fulfillmentDao = fulfillmentDao;
    }

    @Transactional
    public void create(OrderCreationRequest request) {
        Long userId = SignInInterceptor.getUserId();
        Long accountId;
        if (request.getType().equals("BUY")) {
            AccountRecord account = accountDao.findByUserIdAndCurrency(userId, "USD");
            BigDecimal total = request.getAmount().multiply(request.getPrice());
            if (account.getBalance().compareTo(total) < 0) {
                throw new CustomException("Insufficient balance.");
            }
            accountId = account.getId();
            int i = accountDao.decrease(accountId, total);
            if (i < 1) {
                throw new CustomException("Insufficient balance.");
            }
            statementDao.save(new StatementRecord().setAccountId(accountId)
                    .setAmount(total.negate())
                    .setType("BUY"));
        } else {
            AccountRecord account = accountDao.findByUserIdAndCurrency(userId, request.getCurrency());
            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new CustomException("Insufficient balance.");
            }
            accountId = account.getId();
            int i = accountDao.decrease(accountId, request.getAmount());
            if (i < 1) {
                throw new CustomException("Insufficient balance.");
            }
            statementDao.save(new StatementRecord().setAccountId(accountId)
                    .setAmount(request.getAmount().negate())
                    .setType("SELL"));
        }
        orderDao.save(new OrderRecord().setType(request.getType())
                .setAccountId(accountId)
                .setCurrency(request.getCurrency())
                .setPrice(request.getPrice())
                .setAmount(request.getAmount())
                .setAmountLeft(request.getAmount())
                .setCreatedBy(userId));
    }

    @Transactional
    public void match() {
        OrderRecord buyOrder = orderDao.findBuyOrder();
        if (buyOrder == null) {
            return;
        }
        List<OrderRecord> sellOrders = orderDao.findSellOrders(buyOrder.getPrice());
        if (sellOrders.isEmpty()) {
            return;
        }
        for (OrderRecord sellOrder : sellOrders) {
            if (buyOrder.getAmountLeft().equals(BigDecimal.ZERO)) {
                break;
            }
            BigDecimal amount;
            int compareTo = buyOrder.getAmountLeft().compareTo(sellOrder.getAmountLeft());
            if (compareTo > 0) {
                amount = sellOrder.getAmountLeft();
                buyOrder.setAmountLeft(buyOrder.getAmountLeft().subtract(sellOrder.getAmountLeft()));
                buyOrder.setStatus("partially_fulfilled");

                sellOrder.setAmountLeft(BigDecimal.ZERO);
                sellOrder.setStatus("fulfilled");
            } else if (compareTo < 0) {
                amount = buyOrder.getAmountLeft();
                buyOrder.setAmountLeft(BigDecimal.ZERO);
                buyOrder.setStatus("fulfilled");

                sellOrder.setAmountLeft(sellOrder.getAmountLeft().subtract(buyOrder.getAmountLeft()));
                sellOrder.setStatus("partially_fulfilled");
            } else {
                amount = buyOrder.getAmountLeft();
                buyOrder.setAmountLeft(BigDecimal.ZERO);
                buyOrder.setStatus("fulfilled");

                sellOrder.setAmountLeft(BigDecimal.ZERO);
                sellOrder.setStatus("fulfilled");
            }
            fulfillmentDao.save(new FulfillmentRecord().setAmount(amount)
                    .setPrice(sellOrder.getPrice())
                    .setSellOrderId(sellOrder.getId())
                    .setBuyOrderId(buyOrder.getId())
                    .setBuyOrderId(buyOrder.getAccountId())
                    .setSellOrderId(sellOrder.getAccountId())
                    .setCurrency(buyOrder.getCurrency()));
            orderDao.update(buyOrder);
            orderDao.update(sellOrder);
        }
    }
}
