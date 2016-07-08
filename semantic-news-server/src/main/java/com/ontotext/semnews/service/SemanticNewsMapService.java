package com.ontotext.semnews.service;

import com.codepoetics.protonpack.StreamUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.Word;
import org.openrdf.model.datatypes.XMLDatatypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  Service class for processing data from FactForge News remote repository for rendering it as word clouds or a
 *  geographic heat map.
 *
 *  @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 *  @since 11-May-2016
 */
@Service
public class SemanticNewsMapService {

    private static final Logger LOG = LoggerFactory.getLogger(SemanticNewsMapService.class);

    public static final String NOW_NEWS_URI = "http://now.ontotext.com/#document?uri=";


    public static final ImmutableSet<String> PUB_CATEGORIES = ImmutableSet.of(
            "All",
            "Business",
            "Sports",
            "International",
            "Science and Technology",
            "Lifestyle"
    );

    /**
     * Processing results from MOST POPULAR criteria SPARQL queries for word cloud visualisation.
     *
     * @param queryResult
     *          sparql results a (binding)-(list of values) map
     *
     * @return a list of {@link Word} object used for MOST POPULAR criteria word cloud visualisation
     */
    public List<Word> getMostPopularEntities(Map<String, List<String>> queryResult) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        Stream<String> labels = queryResult.get("entityLabel").stream();
        Stream<String> weights = queryResult.get("relativePopularity").stream()
                .map(this::normalizeWeights);
        Stream<String> mentionedEntities = queryResult.get("mentionedLodEntity").stream();

        return zip2Words(labels, weights, mentionedEntities);
    }

    /**
     * Processing results from HIDDEN CHAMPIONS criteria SPARQL queries for word cloud visualisation.
     *
     * @param queryResult
     *          sparql results a (binding)-(list of values) map
     *
     * @return a list of {@link Word} object used for HIDDEN CHAMPIONS criteria word cloud visualisation
     */
    public List<Word> getHiddenChampions(Map<String, List<String>> queryResult) {
        if (queryResult.isEmpty()) {
            return new ArrayList<>();
        }

        Stream<String> labels = queryResult.get("entityLabel").stream();
        Stream<String> weights = queryResult.get("relativePopularity").stream()
                .map(this::normalizeWeights);
        Stream<String> mentionedEntities = queryResult.get("relativeEntity").stream();

        return zip2Words(labels, weights, mentionedEntities);
    }

    /**
     * Processing results for geographic heat map visualisation.
     *
     * @param queryResult
     *          sparql results a (binding)-(list of values) map
     *
     * @return csv response of country codes and mentions frequency counts
     */
    public String getGeoHeatMap(Map<String, List<String>> queryResult) {
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

    /**
     * Preparing a list of news titles, news links and corresponding news categories mentioning
     * a selected country.
     *
     * @param queryResult
     *          sparql results a (binding)-(list of values) map
     *
     * @return csv response of country codes and mentions frequency counts
     */
    public List<NewsEntity> getNewsMentioningCountry(Map<String, List<String>> queryResult) {
        Stream<String> newsTitles = queryResult.get("newsTitle").stream();
        Stream<String> newsUrls = queryResult.get("newsUrl").stream();
        Stream<String> categories = queryResult.get("category").stream();

        return StreamUtils.zip(newsTitles, newsUrls, categories,
                (newsTitle, newsUrl, category) -> {
                    NewsEntity newsEntity = new NewsEntity();
                    newsEntity.setTitle(newsTitle);
                    try {
                        newsEntity.setUrl(NOW_NEWS_URI + URLEncoder.encode(newsUrl, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        LOG.error("Error encoding news url -> {}", newsUrl, e);
                    }
                    newsEntity.setCategory(category);
                    return newsEntity;
                })
                .collect(Collectors.toList());
    }

    public boolean areNewsEntityResultsAvailable(JsonNode newsMentioningEntityResults) {
        JsonNode newsEntityResults = newsMentioningEntityResults.get("results").get("bindings");
        for (JsonNode jsonNode : newsEntityResults) {
            if (jsonNode.get("news") != null) {
                return true;
            }
        }
        return false;
    }

    public String toIsoLocalDate(String dateString) {
        DateTimeFormatter fromFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate parsedDate = LocalDate.parse(dateString, fromFormat);
        return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private List<Word> zip2Words(Stream<String> labels,
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

    private String normalizeWeights(String w) {
        return String.valueOf(Double.parseDouble(w) + 20);
    }
}
