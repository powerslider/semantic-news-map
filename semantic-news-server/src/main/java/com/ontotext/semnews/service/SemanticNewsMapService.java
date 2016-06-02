package com.ontotext.semnews.service;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.Word;
import org.openrdf.model.datatypes.XMLDatatypeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 11-May-2016
 */
@Service
public class SemanticNewsMapService {

    public static final ImmutableMap<String, String> INDUSTRIES = ImmutableMap.<String, String>builder()
            .put("all", "all")
            .put("Agriculture", "http://dbpedia.org/resource/Agriculture")
            .put("Biotechnology", "http://dbpedia.org/resource/Biotechnology")
            .put("Electronics", "http://dbpedia.org/resource/Electronics")
            .put("Telecommunications", "http://dbpedia.org/resource/Telecommunications")
            .put("Real estate", "http://dbpedia.org/resource/Real_estate")
            .put("Internet", "http://dbpedia.org/resource/Internet")
            .put("Software", "http://dbpedia.org/resource/Software")
            .put("Transport", "http://dbpedia.org/resource/Transport")
            .put("Manufacturing", "http://dbpedia.org/resource/Manufacturing")
            .put("Construction", "http://dbpedia.org/resource/Construction")
            .put("Mining", "http://dbpedia.org/resource/Mining")
            .put("Energy", "http://dbpedia.org/resource/Energy")
            .put("Entertainment", "http://dbpedia.org/resource/Entertainment")
            .put("Mass media", "http://dbpedia.org/resource/Mass_media")
            .put("Education", "http://dbpedia.org/resource/Education")
            .put("Finance", "http://dbpedia.org/resource/Finance")
            .put("Automotive", "http://dbpedia.org/resource/Automotive")
            .put("Healthcare", "http://dbpedia.org/resource/Healthcare")
            .put("Publishing", "http://dbpedia.org/resource/Publishing")
            .put("Hospitality", "http://dbpedia.org/resource/Hospitality")
            .put("Retail", "http://dbpedia.org/resource/Retail")
            .put("Oil and gas", "http://dbpedia.org/resource/Oil_and_gas")
            .put("Food and Beverage", "http://dbpedia.org/resource/Food_and_Beverage")
            .build();

    public static final ImmutableMap<String, String> CONTINENTS = ImmutableMap.<String, String>builder()
            .put("Africa", "http://dbpedia.org/resource/Africa")
            .put("Antarctica", "http://dbpedia.org/resource/Antarctica")
            .put("Asia", "http://dbpedia.org/resource/Asia")
            .put("Europe", "http://dbpedia.org/resource/Europe")
            .put("North America", "http://dbpedia.org/resource/North_America")
            .put("South America", "http://dbpedia.org/resource/South_America")
            .put("Australia (continent)", "http://dbpedia.org/resource/Australia_(continent)")
            .build();

    public static final ImmutableSet<String> PUB_CATEGORIES = ImmutableSet.of(
            "All",
            "Business",
            "Sports",
            "International",
            "Science and Technology",
            "Lifestyle"
    );

    public List<Word> getMostFrequentEntities(Map<String, List<String>> queryResult, String from, String category) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        Stream<String> labels = queryResult.get("entityLabel").stream();
        Stream<String> weights = queryResult.get("relativePopularity").stream()
                .map(this::normalizeWeights);
        Stream<String> mentionedEntities = queryResult.get("mentionedLodEntity").stream();

        return zip2Words(from, category, labels, weights, mentionedEntities);
    }

    public List<Word> getHiddenChampions(Map<String, List<String>> queryResult, String from, String category) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        Stream<String> labels = queryResult.get("entityLabel").stream();
        Stream<String> weights = queryResult.get("relativePopularity").stream()
                .map(this::normalizeWeights);
        Stream<String> mentionedEntities = queryResult.get("relativeEntity").stream();

        return zip2Words(from, category, labels, weights, mentionedEntities);
    }

    private List<Word> zip2Words(String from,
                                 String category,
                                 Stream<String> labels,
                                 Stream<String> weights,
                                 Stream<String> mentionedEntities) {
        return StreamUtils.zip(labels, weights, mentionedEntities,
                (label, weight, mentionedEntity) -> {
                    Word word = new Word();
                    word.setText(label);
                    word.setSize(XMLDatatypeUtil.parseDouble(weight));
                    word.setEntityUri(mentionedEntity);
                    return word;
                })
                .collect(Collectors.toList());
    }


    public List<NewsEntity> getNewsMentioningEntity(Map<String, List<String>> queryResult) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        List<NewsEntity> newsEntities = new ArrayList<>();

        List<String> newsTitles = queryResult.get("newsTitle");
        List<String> newsDate = queryResult.get("newsDate");
        List<String> newsUris = queryResult.get("news");
        List<String> entityRelevances = queryResult.get("entityRelevance");

        for (int i = 0; i < newsTitles.size(); i++) {
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setTitle(newsTitles.get(i));
            newsEntity.setDate(newsDate.get(i));
            newsEntity.setUriLink(newsUris.get(i));
            newsEntity.setEntityRelevance(entityRelevances.get(i));
            newsEntities.add(newsEntity);
        }

        return newsEntities;
    }

    public List<NewsEntity> getNewsMentioningRelEntity(Map<String, List<String>> queryResult) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        List<NewsEntity> newsEntities = new ArrayList<>();

        List<String> newsTitles = queryResult.get("newsTitle");
        List<String> newsDate = queryResult.get("newsDate");
        List<String> newsUris = queryResult.get("news");
        List<String> entityRelevances = queryResult.get("relEntity");
        List<String> intermedEntities = queryResult.get("intermedEntity");

        for (int i = 0; i < newsTitles.size(); i++) {
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setTitle(newsTitles.get(i));
            newsEntity.setDate(newsDate.get(i));
            newsEntity.setUriLink(newsUris.get(i));
            newsEntity.setEntityRelevance(entityRelevances.get(i));
            newsEntity.setInternalEntity(intermedEntities.get(i));
            newsEntities.add(newsEntity);
        }

        return newsEntities;
    }

    public String getHeatMap(Map<String, List<String>> queryResult) {
        if (queryResult.isEmpty()) {
            return "";
        }

        Stream<String> labels = queryResult.get("countryCode").stream();
        Stream<String> mentions = queryResult.get("mention").stream();

        String heatMapEntries = StreamUtils.zip(labels, mentions,
                (label, mention) -> String.join(",", label, mention) + "\n")
                .collect(Collectors.joining());

        return String.join("\n", "country_code,frequency", heatMapEntries);
    }

    public String toIsoLocalDate(String dateString) {
        DateTimeFormatter fromFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate parsedDate = LocalDate.parse(dateString, fromFormat);
        return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String normalizeWeights(String w) {
        return String.valueOf(Double.parseDouble(w) + 20);
    }
}
