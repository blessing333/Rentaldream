package com.blessing.lentaldream.modules.account.repository;

import com.blessing.lentaldream.modules.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long>{
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
