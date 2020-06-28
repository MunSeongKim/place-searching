package com.mskim.place_searching.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(length = 50, unique = true, updatable = false)
    private String account;
    @Column
    private String password;

    @Builder
    public User(Long userId, String account, String password) {
        this.userId = userId;
        this.account = account;
        this.password = password;
    }

    // TODO: for local test
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
