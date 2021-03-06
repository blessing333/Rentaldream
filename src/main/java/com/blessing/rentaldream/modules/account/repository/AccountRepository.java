package com.blessing.rentaldream.modules.account.repository;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long>{
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Account findByEmail(String email);
    Account findByNickname(String nickname);
    @Query("SELECT distinct at.account from AccountTag at left join AccountZone az on at.account = az.account where at.tag in :tag and az.zone in :zone")
    List<Account> findAccountWithTagAndZone(@Param("tag") List<Tag> tag, @Param("zone") List<Zone> zone);


}
