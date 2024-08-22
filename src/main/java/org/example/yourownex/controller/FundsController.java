package org.example.yourownex.controller;

import jakarta.transaction.Transactional;
import org.example.yourownex.dto.DepositRequest;
import org.example.yourownex.dto.Result;
import org.example.yourownex.entity.Account;
import org.example.yourownex.entity.Statement;
import org.example.yourownex.repository.AccountRepository;
import org.example.yourownex.repository.StatementRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds")
public class FundsController {
    private final AccountRepository repository;
    private final StatementRepository statementRepository;

    public FundsController(AccountRepository repository, StatementRepository statementRepository) {
        this.repository = repository;
        this.statementRepository = statementRepository;
    }

    @PutMapping("/deposit")
    @Transactional
    public Result<Void> deposit(@RequestBody DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        Account account = repository.findByUserIdAndCurrency(userId, "USD");
        statementRepository.save(new Statement().setAccountId(account.getId())
                .setAmount(request.getAmount())
                .setType("DEPOSIT"));
        account.setTotal(account.getTotal().add(request.getAmount()));
        account.setAvailable(account.getAvailable().add(request.getAmount()));
        repository.save(account);
        return Result.success();
    }

    @PutMapping("/withdraw")
    @Transactional
    public Result<Void> withdraw(@RequestBody DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        Account account = repository.findByUserIdAndCurrency(userId, "USD");
        if (account.getAvailable().compareTo(request.getAmount()) < 0) {
            throw new CustomException("Insufficient balance.");
        }
        statementRepository.save(new Statement().setAccountId(account.getId())
                .setAmount(request.getAmount().negate())
                .setType("DEPOSIT"));
        account.setTotal(account.getTotal().subtract(request.getAmount()));
        account.setAvailable(account.getAvailable().subtract(request.getAmount()));
        repository.save(account);
        return Result.success();
    }
}
