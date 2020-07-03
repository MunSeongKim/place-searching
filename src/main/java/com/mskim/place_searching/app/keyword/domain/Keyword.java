package com.mskim.place_searching.app.keyword.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_search_keyword")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String value;
    @Column(nullable = false)
    private Integer count;

    @Builder
    public Keyword(String value) {
        this.value = value;
    }

    public Keyword increaseCount() {
        this.count = this.count + 1;

        return this;
    }

    @PrePersist
    private void setDefaultCount() {
        this.count = (this.count == null) ? 1 : this.count;
    }
}
