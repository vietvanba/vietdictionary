package com.dictionary.VietDictionary.controller;

import com.dictionary.VietDictionary.payload.SearchResponse;
import com.dictionary.VietDictionary.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String word, @RequestParam(required = false) Integer type) {
        try{
            if (type == null)
                return ResponseEntity.ok(searchService.findByWord(word.toLowerCase()));
            else
                return ResponseEntity.ok(searchService.findByWord(word.toLowerCase(), type));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
