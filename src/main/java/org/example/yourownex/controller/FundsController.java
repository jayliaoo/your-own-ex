package org.example.yourownex.controller;

import org.example.yourownex.dao.AccountService;
import org.example.yourownex.dao.StatementService;
import org.example.yourownex.dto.DepositRequest;
import org.example.yourownex.dto.Result;
import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.example.yourownex.jooq.tables.records.StatementRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds")
public class FundsController {
    private final AccountService accountService;
    private final StatementService statementService;

    public FundsController(
            AccountService accountService,
            StatementService statementService
    ) {
        this.accountService = accountService;
        this.statementService = statementService;
    }

    @PutMapping("/deposit")
    @Transactional
    public Result<Void> deposit(@RequestBody DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        AccountRecord account = accountService.findByUserIdAndCurrency(userId, "USD");
        StatementRecord statement = new StatementRecord();
        statement.setAccountId(account.getId());
        statement.setAmount(request.getAmount());
        statement.setType("DEPOSIT");
        statementService.save(statement);
        account.setTotal(account.getTotal().add(request.getAmount()));
        account.setAvailable(account.getAvailable().add(request.getAmount()));
        accountService.update(account);
        return Result.success();
    }

    @PutMapping("/withdraw")
    @Transactional
    public Result<Void> withdraw(@RequestBody DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        AccountRecord account = accountService.findByUserIdAndCurrency(userId, "USD");
        if (account.getAvailable().compareTo(request.getAmount()) < 0) {
            throw new CustomException("Insufficient balance.");
        }
        statementService.save(new StatementRecord().setAccountId(account.getId())
                .setAmount(request.getAmount().negate())
                .setType("WITHDRAW"));
        account.setTotal(account.getTotal().subtract(request.getAmount()));
        account.setAvailable(account.getAvailable().subtract(request.getAmount()));
        accountService.update(account);
        return Result.success();
    }
}
