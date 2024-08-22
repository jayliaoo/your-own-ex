package org.example.yourownex.repository;

import org.example.yourownex.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserIdAndCurrency(Long userId,String currency);
}
