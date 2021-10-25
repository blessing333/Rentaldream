package com.blessing.rentaldream.modules.account.repository;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.domain.AccountZone;
import com.blessing.rentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountZoneRepository extends JpaRepository<AccountZone,Long> {
    AccountZone findByAccountAndZone(Account account, Zone zone);
}
