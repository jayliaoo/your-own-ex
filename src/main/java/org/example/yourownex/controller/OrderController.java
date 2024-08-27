//package org.example.yourownex.controller;
//
//import jakarta.transaction.Transactional;
//import org.example.yourownex.dto.OrderCreationRequest;
//import org.example.yourownex.dto.Result;
//import org.example.yourownex.entity.Account;
//import org.example.yourownex.entity.Order;
//import org.example.yourownex.entity.Statement;
//import org.example.yourownex.repository.AccountRepository;
//import org.example.yourownex.repository.OrderRepository;
//import org.example.yourownex.repository.StatementRepository;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//
//@RestController
//@RequestMapping("/order")
//public class OrderController {
//    private final OrderRepository repository;
//    private final AccountRepository accountRepository;
//    private final StatementRepository statementRepository;
//
//    public OrderController(
//            OrderRepository repository,
//            AccountRepository accountRepository,
//            StatementRepository statementRepository
//    ) {
//        this.repository = repository;
//        this.accountRepository = accountRepository;
//        this.statementRepository = statementRepository;
//    }
//
//    @PostMapping
//    @Transactional
//    public Result<Void> create(@RequestBody OrderCreationRequest request) {
//        Long userId = SignInInterceptor.getUserId();
//        if (request.getType().equals("BUY")) {
//            Account account = accountRepository.findByUserIdAndCurrency(userId, "USD");
//            BigDecimal total = request.getAmount().multiply(request.getPrice());
//            if (account.getAvailable().compareTo(total) < 0) {
//                throw new CustomException("Insufficient balance.");
//            }
//            statementRepository.save(new Statement().setAccountId(account.getId())
//                    .setAmount(total.negate())
//                    .setType("BUY"));
//            account.setTotal(account.getTotal().add(request.getAmount()));
//            account.setAvailable(account.getAvailable().add(request.getAmount()));
//            accountRepository.save(account);
//        }
//        repository.save(new Order().setType(request.getType())
//                .setCurrency(request.getCurrency())
//                .setPrice(request.getPrice())
//                .setAmount(request.getAmount())
//                .setAmountLeft(request.getAmount())
//                .setCreatedBy(userId));
//        return Result.success();
//    }
//}
