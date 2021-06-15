package com.blessing.lentaldream.modules.account.repository;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountZone;
import com.blessing.lentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountZoneRepository extends JpaRepository<AccountZone,Long> {
    AccountZone findByAccountAndZone(Account account, Zone zone);
}
