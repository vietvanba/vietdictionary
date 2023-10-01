package com.dictionary.VietDictionary.service;

import com.dictionary.VietDictionary.exception.NotFoundOxford;
import com.dictionary.VietDictionary.payload.SearchResponse;
import com.dictionary.VietDictionary.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final OxfordDictionaryService oxfordDictionaryService;

    public SearchResponse extractDataFromOxford(String word) {
        try {
            return searchRepository.save(oxfordDictionaryService.fetchWordDefinition(word));
        } catch (Exception e) {
            throw new NotFoundOxford(word, e);
        }
    }

    public SearchResponse findByWord(String word) {
        SearchResponse response = searchRepository.findById(word).orElse(null);
        if (response == null) {
            response = extractDataFromOxford(word);
        }
        return response;
    }

    public SearchResponse findByWord(String word, Integer type) {
        SearchResponse response = null;
        if (type.equals(1)) {
            response = searchRepository.findById(word).orElse(null);
            if (response == null) {
                response = extractDataFromOxford(word);
            }
        } else {
            response = extractDataFromOxford(word);
        }

        return response;
    }
}
