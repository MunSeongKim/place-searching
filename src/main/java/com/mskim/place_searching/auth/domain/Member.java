package com.mskim.place_searching.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "t_member")
@Getter
@ToString //TODO: For logging
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(length = 50, unique = true, updatable = false)
    private String account;
    @Column
    private String password;

    @Builder
    public Member(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
