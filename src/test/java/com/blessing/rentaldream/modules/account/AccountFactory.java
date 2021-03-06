package com.blessing.rentaldream.modules.account;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    private final AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account newAccount = Account.createNewAccount(nickname,"testmail@gmail.com","testPass");
        accountRepository.save(newAccount);
        return newAccount;
    }

}
