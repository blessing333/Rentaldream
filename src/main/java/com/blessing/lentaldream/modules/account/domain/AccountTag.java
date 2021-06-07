package com.blessing.lentaldream.modules.account.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.swing.text.html.HTML;

@Entity
public class AccountTag {
    @Id
    private Long id;

    @ManyToOne
    private Account account;
    @ManyToOne
    private Tag tag;

}
