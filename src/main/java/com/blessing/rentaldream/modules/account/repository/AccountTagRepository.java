package com.blessing.rentaldream.modules.account.repository;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.domain.AccountTag;
import com.blessing.rentaldream.modules.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTagRepository extends JpaRepository<AccountTag,Long> {

    AccountTag findByAccountAndTag(Account account, Tag tag);
}
