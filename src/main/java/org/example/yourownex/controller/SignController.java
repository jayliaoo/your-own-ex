//package org.example.yourownex.controller;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.transaction.Transactional;
//import lombok.SneakyThrows;
//import org.example.yourownex.dto.Result;
//import org.example.yourownex.dto.SigninRequest;
//import org.example.yourownex.dto.UserRequest;
//import org.example.yourownex.entity.Account;
//import org.example.yourownex.entity.User;
//import org.example.yourownex.repository.AccountRepository;
//import org.example.yourownex.repository.UserRepository;
//import org.example.yourownex.service.JWTService;
//import org.example.yourownex.service.MacService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/sign")
//public class SignController {
//    private final UserRepository userRepository;
//    private final AccountRepository accountRepository;
//    private final MacService macService;
//    private final JWTService jwtService;
//
//    public SignController(UserRepository userRepository,
//                          AccountRepository accountRepository, MacService macService, JWTService jwtService) {
//        this.userRepository = userRepository;
//        this.accountRepository = accountRepository;
//        this.macService = macService;
//        this.jwtService = jwtService;
//    }
//
//    @SneakyThrows
//    @PostMapping("/up")
//    @Transactional
//    public Result<Long> signup(@RequestBody UserRequest request) {
//        User user = new User()
//                .setEmail(request.getEmail())
//                .setName(request.getName())
//                .setPassword(macService.mac(request.getPassword()));
//        userRepository.save(user);
//
//        accountRepository.save(new Account().setUserId(user.getId()).setCurrency("USD"));
//        return Result.success(user.getId());
//    }
//
//    @SneakyThrows
//    @PostMapping("/in")
//    public ResponseEntity<Result<String>> signin(
//            @RequestBody SigninRequest request,
//            HttpSession httpSession
//    ) {
//        User user = userRepository.findByEmailAndPassword(request.getEmail(),
//                macService.mac(request.getPassword()));
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>(Result.success(jwtService.getToken(user.getId())), HttpStatus.OK);
//    }
//}
