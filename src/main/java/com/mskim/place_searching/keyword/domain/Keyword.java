package com.mskim.place_searching.keyword.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "t_search_keyword")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;
    @Column(nullable = false, unique = true, updatable = false)
    private String value;
    @Column(nullable = false)
    private Integer count;

    @Builder
    public Keyword(String value, Integer count) {
        this.value = value;
        this.count = count;
    }

    public void increaseCount() {
        this.count = this.count + 1;
    }

    @PrePersist
    private void setDefaultCount() {
        this.count = (this.count == null) ? 1 : this.count;
    }
}
