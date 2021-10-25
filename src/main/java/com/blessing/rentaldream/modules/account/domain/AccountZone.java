package com.blessing.rentaldream.modules.account.domain;

import com.blessing.rentaldream.modules.zone.Zone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
public class AccountZone {
    @Id @GeneratedValue @Column(name = "ACCOUNT_ZONE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    public static AccountZone createNewAccountZone(Account account, Zone zone) {
        AccountZone instance = new AccountZone();
        instance.account = account;
        instance.zone = zone;
        return instance;
    }
}
