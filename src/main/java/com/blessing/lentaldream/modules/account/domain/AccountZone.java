package com.blessing.lentaldream.modules.account.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AccountZone {
    @Id
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Zone zone;

}
