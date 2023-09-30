package com.dictionary.VietDictionary.controller;

import com.dictionary.VietDictionary.payload.SearchResponse;
import com.dictionary.VietDictionary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public SearchResponse search(@RequestParam String word, @RequestParam(required = false) Integer type) {
        if (type == null)
            return searchService.findByWord(word.toLowerCase());
        else
            return searchService.findByWord(word.toLowerCase(), type);
    }
}
