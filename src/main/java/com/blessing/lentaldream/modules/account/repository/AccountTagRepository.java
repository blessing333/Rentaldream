package com.blessing.lentaldream.modules.account.repository;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountTag;
import com.blessing.lentaldream.modules.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTagRepository extends JpaRepository<AccountTag,Long> {

    AccountTag findByAccountAndTag(Account account, Tag tag);
}
