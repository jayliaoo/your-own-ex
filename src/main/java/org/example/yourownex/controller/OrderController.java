package org.example.yourownex.controller;

import org.example.yourownex.dao.AccountService;
import org.example.yourownex.dao.OrderService;
import org.example.yourownex.dao.StatementService;
import org.example.yourownex.dto.OrderCreationRequest;
import org.example.yourownex.dto.Result;
import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.example.yourownex.jooq.tables.records.OrderRecord;
import org.example.yourownex.jooq.tables.records.StatementRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final AccountService accountService;
    private final StatementService statementService;

    public OrderController(
            OrderService orderService,
            AccountService accountService,
            StatementService statementService
    ) {
        this.orderService = orderService;
        this.accountService = accountService;
        this.statementService = statementService;
    }

    @PostMapping
    @Transactional
    public Result<Void> create(@RequestBody OrderCreationRequest request) {
        Long userId = SignInInterceptor.getUserId();
        if (request.getType().equals("BUY")) {
            AccountRecord account = accountService.findByUserIdAndCurrency(userId, "USD");
            BigDecimal total = request.getAmount().multiply(request.getPrice());
            if (account.getAvailable().compareTo(total) < 0) {
                throw new CustomException("Insufficient balance.");
            }
            statementService.save(new StatementRecord().setAccountId(account.getId())
                    .setAmount(total.negate())
                    .setType("BUY"));
            account.setTotal(account.getTotal().add(request.getAmount()));
            account.setAvailable(account.getAvailable().add(request.getAmount()));
            accountService.update(account);
        }
        orderService.save(new OrderRecord().setType(request.getType())
                .setCurrency(request.getCurrency())
                .setPrice(request.getPrice())
                .setAmount(request.getAmount())
                .setAmountLeft(request.getAmount())
                .setCreatedBy(userId));
        return Result.success();
    }
}
