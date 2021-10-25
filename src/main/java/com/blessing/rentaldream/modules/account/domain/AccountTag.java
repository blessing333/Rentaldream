package com.blessing.rentaldream.modules.account.domain;

import com.blessing.rentaldream.modules.tag.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class AccountTag {
    @Id @GeneratedValue @Column(name = "ACCOUNT_TAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    static public AccountTag createNewAccountTag(Account account, Tag tag){
        AccountTag instance = new AccountTag();
        instance.setAccount(account);
        instance.setTag(tag);
        return instance;
    }

}
