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
public class Phonic {
    private String mp3_url;
    private String pronunciation;
}
