package com.mskim.place_searching.keyword.service;

import com.mskim.place_searching.keyword.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }
}
