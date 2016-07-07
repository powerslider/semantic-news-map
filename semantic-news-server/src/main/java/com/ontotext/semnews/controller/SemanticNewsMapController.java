package com.ontotext.semnews.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.Word;
import com.ontotext.semnews.service.SemanticNewsMapService;
import com.ontotext.semnews.service.SparqlService;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 *  Controller class for providing data from FactForge News remote repository for rendering it as word clouds or a
 *  geographic heat map.
 *
 *  @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 *  @since 11-May-2016
 */
@RestController
@RequestMapping("/rest/semnews")
public class SemanticNewsMapController {

    @Autowired
    private SparqlService sparqlService;

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;


    /**
     * Endpoint for serving news data processed for word cloud visualisation
     * based on different popularity criteria.
     *
     * @param from
     *          date of news publication
     * @param type
     *          word cloud criteria type: popular, hidden, popularAndHidden
     * @param category
     *          news category: Lifistyle, Business, etc.
     * @param relPopularity
     *          flag to trigger relative popularity calculation or not
     *
     * @return a list of {@link Word} objects serialized as json
     */
    @RequestMapping(value = "/word-cloud", method = RequestMethod.GET)
    public ResponseEntity<List<Word>> getWordCloud(@RequestParam("from") String from,
                                   @RequestParam(value = "type", required = false, defaultValue = "popularAndHidden") String type,
                                   @RequestParam("category") String category,
                                   @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity) {

        List<Word> words = sparqlService.new WithConnection<List<Word>>() {

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
                            queryResults = executeWordCloudQuery("mostPopularRelative");
                        } else {
                            queryResults = executeWordCloudQuery("mostPopular");
                        }
                        return semanticNewsMapService.getMostPopularEntities(queryResults);
                    case "hidden":
                        queryResults = executeWordCloudQuery("hiddenChampions");
                        return semanticNewsMapService.getHiddenChampions(queryResults);
                    default:
                        queryResults = executeWordCloudQuery("hiddenAndPopular");
                        return semanticNewsMapService.getHiddenChampions(queryResults);
                }
            }
        }.run();

        if (words.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(words, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Set<String> getCategories() {
        return SemanticNewsMapService.PUB_CATEGORIES;
    }

    /**
     * Endpoint for serving news data processed for geographic heat map visualisation
     * based on news country mentions frequency.
     *
     * @param from
     *          date of news publication
     *
     * @return csv response of country codes and mentions frequency counts
     */
    @RequestMapping(value = "/world-heat-map", method = RequestMethod.GET, produces = "text/csv")
    public ResponseEntity<String> getWorldHeatMap(@RequestParam("from") String from) {
        String geoHeatMapData = sparqlService.new WithConnection<String>() {

            @Override
            protected String doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults = executeQueryAndGetBindings("newsByCountry",
                        q -> q.replace("{min_date}", from)
                                .replace("{max_date}", from));

                return semanticNewsMapService.getGeoHeatMap(queryResults);
            }
        }.run();

        if (Strings.isNullOrEmpty(geoHeatMapData)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(geoHeatMapData, HttpStatus.OK);
    }

    /**
     * Endpoint for serving news details data processed for sparql results table visualisation
     * base on two criteria: news mentioning entities and news mentioning related entities.
     *
     * @param entityUri
     *          RDF URI of examined news article
     * @param from
     *          publication date of news mentioning entities/related entities from the examined news article
     * @param category
     *          category of news mentioning entities/related entities from the examined news article
     *
     *  @return json response of mime type application/sparql-results+json for feeding YASR js library table view
     */
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

    /**
     * Endpoint for serving a list of news articles mentioning a selected country.
     *
     * @param countryCode
 *              selected country country code
     * @param from
     *          publication date of news articles mentioning the selected country
     *
     * @return a list of {@link NewsEntity} objects serialized as json
     */
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
