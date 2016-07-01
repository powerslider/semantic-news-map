package com.ontotext.semnews.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.Word;
import com.ontotext.semnews.service.SemanticNewsMapService;
import com.ontotext.semnews.service.SparqlService;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/rest/semnews")
public class SemanticNewsMapController {

    @Autowired
    private SparqlService sparqlService;

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;

    @RequestMapping(value = "/word-cloud", method = RequestMethod.GET)
    public List<Word> getWordCloud(@RequestParam("from") String from,
                                   @RequestParam(value = "type", required = false, defaultValue = "popularAndHidden") String type,
                                   @RequestParam("category") String category,
                                   @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity) {

        return sparqlService.new WithConnection<List<Word>>() {

            private Map<String, List<String>> executeWordCloudQuery(String sparqlFileName) {
                return executeQueryAndGetBindings(
                        sparqlFileName,
                        q -> q.replace("{category}", category)
                                .replace("{min_date}", from)
                                .replace("{max_date}", from));
            }

            @Override
            protected List<Word> doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults;
                switch (type) {
                    case "popular":
                        if (relPopularity) {
                            queryResults = executeWordCloudQuery("mostPopularNonNormalized");
                            return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                        } else {
                            queryResults = executeWordCloudQuery("wordCloud");
                            return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                        }
                    case "hidden":
                        queryResults = executeWordCloudQuery("hiddenChampions");
                        return semanticNewsMapService.getHiddenChampions(queryResults, from, category);
                    default:
                        queryResults = executeWordCloudQuery("hiddenAndPopular");
                        return semanticNewsMapService.getHiddenChampions(queryResults, from, category);
                }
            }
        }.run();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Set<String> getCategories() {
        return SemanticNewsMapService.PUB_CATEGORIES;
    }

    @RequestMapping(value = "/world-heat-map", method = RequestMethod.GET, produces = "text/csv")
    public String getWorldHeatMap(@RequestParam("from") String from) {
        return sparqlService.new WithConnection<String>() {

            @Override
            protected String doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults = executeQueryAndGetBindings(
                        "newsByCountry",
                        q -> q.replace("{min_date}", from)
                                .replace("{max_date}", from));

                return semanticNewsMapService.getHeatMap(queryResults);
            }
        }.run();
    }

    @RequestMapping(value = "/news-details", method = RequestMethod.GET)
    public Map<String, JsonNode> getNewsEntityDetails(@RequestParam("uri") String entityUri,
                                                      @RequestParam("from") String from,
                                                      @RequestParam("category") String category) {

        String newsMentioningEntityQuery = sparqlService.setQueryPlaceholders(
                "newsMentioningEntity",
                q -> q.replace("{category}", category)
                        .replace("{min_date}", from)
                        .replace("{max_date}", from)
                        .replace("{entity}", entityUri));

        String newsMentioningRelevantEntitiesQuery = sparqlService.setQueryPlaceholders(
                "newsMentioningRelevantEntities",
                q -> q.replaceAll(Pattern.quote("{category}"), category)
                        .replaceAll(Pattern.quote("{min_date}"), from)
                        .replaceAll(Pattern.quote("{max_date}"), from)
                        .replaceAll(Pattern.quote("{entity}"), entityUri));

        JsonNode newsMentioningEntityResults = sparqlService
                .getSparqlResultsAsJson(newsMentioningEntityQuery);
        JsonNode newsMentioningRelevantEntitiesResults = sparqlService
                .getSparqlResultsAsJson(newsMentioningRelevantEntitiesQuery);

        return ImmutableMap.<String, JsonNode>builder()
                .put("nonRelevant", newsMentioningEntityResults)
                .put("relevant", newsMentioningRelevantEntitiesResults)
                .build();
    }

    @RequestMapping(value = "/news-mentioning-country", method = RequestMethod.GET)
    public List<NewsEntity> getNewsMentioningCountry(@RequestParam("countryCode") String countryCode,
                                                     @RequestParam("from") String from) {
        return sparqlService.new WithConnection<List<NewsEntity>>() {
            @Override
            protected List<NewsEntity> doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults = executeQueryAndGetBindings("newsMentioningCountry",
                        q -> q.replace("{min_date}", from)
                                .replace("{max_date}", from)
                                .replace("{country_code}", countryCode));
                return semanticNewsMapService.getNewsMentioningCountry(queryResults);
            }

        }.run();
    }
}
