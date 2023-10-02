package com.dictionary.authentication.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sense {
    private String shortcut;
    private String sense;
    private List<String> examples = new ArrayList<>();
}
