package com.dictionary.VietDictionary.repository;

import com.dictionary.VietDictionary.payload.SearchResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchResponse,String> {
}
