package org.example.yourownex.controller;

import org.example.yourownex.dao.AccountDao;
import org.example.yourownex.dao.StatementDao;
import org.example.yourownex.dto.DepositRequest;
import org.example.yourownex.dto.Result;
import org.example.yourownex.entity.Account;
import org.example.yourownex.entity.Statement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds")
public class FundsController {
    private final AccountDao accountDao;
    private final StatementDao statementDao;

    public FundsController(
            AccountDao accountDao,
            StatementDao statementDao
    ) {
        this.accountDao = accountDao;
        this.statementDao = statementDao;
    }

    @PutMapping("/deposit")
    @Transactional
    public Result<Void> deposit(@RequestBody DepositRequest request) {
        Long userId = SignInInterceptor.getUserId();
        Account account = accountDao.findByUserIdAndCurrency(userId, "USD");
        Statement statement = new Statement();
        statement.setAccountId(account.getId());
        statement.setAmount(request.getAmount());
        statement.setType("DEPOSIT");
        statementDao.save(statement);
        account.setTotal(account.getTotal().add(request.getAmount()));
        account.setAvailable(account.getAvailable().add(request.getAmount()));
        accountDao.save(account);
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
