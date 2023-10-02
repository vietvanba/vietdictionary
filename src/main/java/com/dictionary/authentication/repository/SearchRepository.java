package com.dictionary.authentication.repository;

import com.dictionary.authentication.payload.SearchResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchResponse,String> {
}
