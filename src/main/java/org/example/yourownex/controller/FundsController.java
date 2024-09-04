package org.example.yourownex.controller;

import org.example.yourownex.dto.*;
import org.example.yourownex.service.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funds")
public class FundsController {
    private final FundsService fundsService;

    public FundsController(FundsService fundsService) {
        this.fundsService = fundsService;
    }

    @PutMapping("/deposit")
    public Result<Void> deposit(@RequestBody DepositRequest request) {
        fundsService.deposit(request);
        return Result.success();
    }

    @RequestMapping("/deposit/prepare")
    public Result<String> depositPrepare(String currency) {
        return Result.success(fundsService.depositPrepare(currency));
    }

    @PutMapping("/withdraw")
    @Transactional
    public Result<Void> withdraw(@RequestBody DepositRequest request) {
        fundsService.withdraw(request);
        return Result.success();
    }
}
