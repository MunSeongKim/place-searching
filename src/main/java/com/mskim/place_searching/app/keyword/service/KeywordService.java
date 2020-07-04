package com.mskim.place_searching.app.keyword.service;

import com.mskim.place_searching.app.keyword.domain.Keyword;
import com.mskim.place_searching.app.keyword.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @CacheEvict(value = "keyword", allEntries = true)
    @Transactional
    public void storeKeyword(String placeName) {
        Optional<Keyword> keywordEntity = this.keywordRepository.findByValue(placeName);

        if (keywordEntity.isPresent()) {
            this.keywordRepository.save(keywordEntity.get().increaseCount());
        } else {
            this.keywordRepository.save(Keyword.builder().value(placeName).build());
        }
    }

    @Cacheable(value = "keyword")
    public List<Keyword> retrieveHotKeywords() {
        return this.keywordRepository.findTop10ByOrderByCountDesc();
    }
}
