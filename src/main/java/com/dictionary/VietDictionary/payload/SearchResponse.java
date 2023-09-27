package com.dictionary.VietDictionary.payload;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SearchModel {
    private String word;
    Phonic us;
    Phonic uk;
}
