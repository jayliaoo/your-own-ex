package org.example.yourownex.service;

import org.example.yourownex.dao.*;
import org.example.yourownex.dto.*;
import org.example.yourownex.jooq.tables.records.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UserService {
    private final MacService macService;
    private final UserDao userDao;
    private final AccountDao accountDao;

    public UserService(MacService macService, UserDao userDao, AccountDao accountDao) {
        this.macService = macService;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @Transactional
    public Long signup(UserRequest request) {
        UserRecord user = new UserRecord()
                .setEmail(request.getEmail())
                .setName(request.getName())
                .setPassword(macService.mac(request.getPassword()));
        Long userId = userDao.save(user);

        accountDao.save(new AccountRecord().setUserId(userId).setCurrency("USD"));
        return userId;
    }

    public Long signin(SigninRequest request) {
        UserRecord user = userDao.findByEmailAndPassword(request.getEmail(),
                macService.mac(request.getPassword()));
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}
