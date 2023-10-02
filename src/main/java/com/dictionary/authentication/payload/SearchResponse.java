package com.dictionary.authentication.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "dictionary")
public class SearchResponse {
    @Id
    private String word;
    private String partOfSpeech;
    private Phonic us = new Phonic();
    private Phonic uk = new Phonic();
    private List<Sense> senses = new ArrayList<>();
    private List<String> images = new ArrayList<>();
}
