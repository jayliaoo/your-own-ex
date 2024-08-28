package org.example.yourownex.controller;

import lombok.*;
import org.example.yourownex.dto.*;
import org.example.yourownex.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignController {
    private final UserService userService;

    public SignController(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @PostMapping("/up")
    public Result<Long> signup(@RequestBody UserRequest request) {
        return Result.success(userService.signup(request));
    }

    @SneakyThrows
    @PostMapping("/in")
    public ResponseEntity<Result<String>> signin(@RequestBody SigninRequest request) {
        String token = userService.signin(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Result.success(token), HttpStatus.OK);
    }
}
