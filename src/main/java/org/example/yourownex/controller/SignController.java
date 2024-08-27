package org.example.yourownex.controller;

import lombok.SneakyThrows;
import org.example.yourownex.dao.AccountService;
import org.example.yourownex.dao.UserService;
import org.example.yourownex.dto.Result;
import org.example.yourownex.dto.SigninRequest;
import org.example.yourownex.dto.UserRequest;
import org.example.yourownex.jooq.tables.records.AccountRecord;
import org.example.yourownex.jooq.tables.records.UserRecord;
import org.example.yourownex.service.JWTService;
import org.example.yourownex.service.MacService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class SignController {
    private final MacService macService;
    private final JWTService jwtService;
    private final UserService userService;
    private final AccountService accountService;

    public SignController(
            MacService macService,
            JWTService jwtService,
            UserService userService,
            AccountService accountService
    ) {
        this.macService = macService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.accountService = accountService;
    }

    @SneakyThrows
    @PostMapping("/up")
    @Transactional
    public Result<Long> signup(@RequestBody UserRequest request) {
        UserRecord user = new UserRecord()
                .setEmail(request.getEmail())
                .setName(request.getName())
                .setPassword(macService.mac(request.getPassword()));
        Long userId = userService.save(user);

        accountService.save(new AccountRecord().setUserId(userId).setCurrency("USD"));
        return Result.success(user.getId());
    }

    @SneakyThrows
    @PostMapping("/in")
    public ResponseEntity<Result<String>> signin(@RequestBody SigninRequest request) {
        UserRecord user = userService.findByEmailAndPassword(request.getEmail(),
                macService.mac(request.getPassword()));
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Result.success(jwtService.getToken(user.getId())), HttpStatus.OK);
    }
}
