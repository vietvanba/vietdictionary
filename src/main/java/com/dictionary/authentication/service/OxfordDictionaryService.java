package com.dictionary.authentication.service;

import com.dictionary.authentication.payload.SearchResponse;
import com.dictionary.authentication.payload.Sense;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OxfordDictionaryService {
    @Value("${pixabay.api}")
    private String api;
    @Value("${pixabay.key}")
    private String apiKey;
    public List<String> images(String word) {

        List<String> images = new ArrayList<>();
        String apiUrl = api+word;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(responseBody).getAsJsonObject();

            // Truy cập vào mảng "photos"
            JsonArray photosArray = jsonObject.getAsJsonArray("photos");

            // Lặp qua từng phần tử trong mảng và lấy URL
            for (int i = 0; i < photosArray.size(); i++) {
                JsonObject photoObject = photosArray.get(i).getAsJsonObject();
                String url = photoObject.get("src").getAsJsonObject().get("landscape").getAsString();
                images.add(url);
            }
        } else {
            System.err.println("Lỗi trong quá trình gọi API: " + response.getStatusCode());
        }
        return images;
    }

    public SearchResponse fetchWordDefinition(String word) {
        List<String> images = images(word);
        String url = "http://www.oxfordlearnersdictionaries.com/definition/english/" + word;
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            SearchResponse entry = new SearchResponse();

            Elements divs = document.select("div.entry");
            for (Element div : divs) {
                entry.setWord(div.select("h1.headword").text().toLowerCase());
                entry.setPartOfSpeech(div.select("span.pos").text());
                Element phonBrDiv = div.selectFirst("div.phons_br");
                if (phonBrDiv != null) {
                    entry.getUk().setMp3_url(phonBrDiv.select("div.sound.audio_play_button.pron-uk.icon-audio").attr("data-src-mp3"));
                    entry.getUk().setPronunciation(phonBrDiv.select("span.phon").text());
                }
                Element phonAmDiv = div.selectFirst("div.phons_n_am");
                if (phonAmDiv != null) {
                    entry.getUs().setMp3_url(phonAmDiv.select("div.sound.audio_play_button.pron-us.icon-audio").attr("data-src-mp3"));
                    entry.getUs().setPronunciation(phonAmDiv.select("span.phon").text());
                }
                Elements senses = div.select("li.sense");
                List<Sense> senseList = new ArrayList<>();
                for (Element sense : senses) {

                    String senseText = sense.select("span.def").text();
                    Elements examples = sense.select("span.x");
                    List<String> exampleList = new ArrayList<>();
                    for (Element example : examples) {
                        exampleList.add(example.text());
                    }
                    senseList.add(new Sense(null, senseText, exampleList));
                }
                entry.setSenses(senseList);

                Elements shcuts = div.select("span.shcut-g");
                if (!shcuts.isEmpty()) {
                    List<Sense> shortcutSenses = new ArrayList<>();
                    for (Element shcut : shcuts) {
                        String shcutText = shcut.select("h2.shcut").text();
                        String senseText = shcut.select("span.def").text();
                        Elements shcutExamples = shcut.select("span.x");
                        List<String> exampleList = new ArrayList<>();
                        for (Element example : shcutExamples) {
                            exampleList.add(example.text());
                        }
                        shortcutSenses.add(new Sense(shcutText, senseText, exampleList));
                    }
                    entry.setSenses(shortcutSenses);
                }
            }
            entry.setImages(images);
            return entry;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
