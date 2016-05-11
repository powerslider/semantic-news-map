package com.ontotext.semnews.controller;

import com.google.common.collect.ImmutableMap;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.Word;
import com.ontotext.semnews.model.WorldHeatMap;
import com.ontotext.semnews.service.SemanticNewsMapService;
import com.ontotext.semnews.service.SparqlService;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/rest/semnews")
public class SemanticNewsMapController {

    @Autowired
    private SparqlService sparqlService;

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;

    @RequestMapping(value = "/word-cloud", method = RequestMethod.GET)
    public List<Word> getWordCloud(@RequestParam("from") String from,
                                   @RequestParam("hidden") String hidden,
                                   @RequestParam("category") String category,
                                   @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity) {

        return sparqlService.new WithConnection<List<Word>>() {

            private Map<String, List<String>> executeWordCloudQuery(String sparqlFileName) {
                return executeQueryAndGetBindings(
                        sparqlFileName,
                        q -> q.replace("{category}", category)
                                .replace("{min_date}", semanticNewsMapService.toIsoLocalDate(from))
                                .replace("{max_date}", semanticNewsMapService.toIsoLocalDate(from)));
            }

            @Override
            protected List<Word> doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults;
                switch (hidden) {
                    case "false":
                        if (relPopularity) {
                            queryResults = executeWordCloudQuery("mostPopularNonNormalized");
                            return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                        } else {
                            queryResults = executeWordCloudQuery("wordCloud");
                            return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                        }
                    case "true":
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

    @RequestMapping(value = "/world-heat-map", method = RequestMethod.GET)
    public List<WorldHeatMap> getWorldHeatMap(@RequestParam("from") String from) {
        return sparqlService.new WithConnection<List<WorldHeatMap>>() {

            @Override
            protected List<WorldHeatMap> doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResults = executeQueryAndGetBindings("newsByCountry",
                        q -> q.replace("{min_date}", semanticNewsMapService.toIsoLocalDate(from))
                                .replace("{max_date}", semanticNewsMapService.toIsoLocalDate(from)));
                return semanticNewsMapService.getHeatMap(queryResults);
            }
        }.run();
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public Map<String, List<NewsEntity>> getNewsEntityDetails(@RequestParam(value = "uri") String entitiUri,
                                                              @RequestParam("from") String from,
                                                              @RequestParam("category") String category) {

        return sparqlService.new WithConnection<Map<String, List<NewsEntity>>>() {

            private Map<String, List<String>> executeNewsEntityDetailsQuery(String sparqlFileName) {
                return executeQueryAndGetBindings(
                        sparqlFileName,
                        q -> q.replace("{category}", category)
                                .replace("{min_date}", semanticNewsMapService.toIsoLocalDate(from))
                                .replace("{max_date}", semanticNewsMapService.toIsoLocalDate(from))
                                .replace("{entity}", entitiUri));
            }

            @Override
            protected Map<String, List<NewsEntity>> doInConnection() throws RepositoryException {
                Map<String, List<String>> queryResult1 = executeNewsEntityDetailsQuery("newsMentioningEntity");
                List<NewsEntity> newsMentioningEntity = semanticNewsMapService.getNewsMentioningEntity(queryResult1);
                Map<String, List<String>> queryResult2 = executeNewsEntityDetailsQuery("newsMentioningRelevantEntities");
                List<NewsEntity> newsMentioningRelEntity = semanticNewsMapService.getNewsMentioningRelEntity(queryResult2);

                return ImmutableMap.<String, List<NewsEntity>>builder()
                        .put("nonRelevant", newsMentioningEntity)
                        .put("relevant", newsMentioningRelEntity)
                        .build();
            }
        }.run();
    }

    //
//	@RequestMapping(value = "/tag-cloud/results", method = RequestMethod.GET)
//	public String getReuslts(HttpServletRequest request, @RequestParam("from") String from,
//							 @RequestParam("category") String category,
//							 @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity,
//							 ModelMap model) {
//		model.addAttribute("fromA", from);
////		model.addAttribute("toA", to);
//		model.addAttribute("catA", category);
//
//		model.addAttribute("repativePop", relPopularity);
//
//
//		return "tagcloud";
//
//	}
//
//	@RequestMapping(value = "/heat-map", method = RequestMethod.GET)
//	@ResponseBody
//	public List<WorldHeatMap> getHeatMap(HttpServletRequest request, @RequestParam("from") String from, ModelMap model) {
//		return semanticNewsMapService.getHeatMap(from);
//	}

}
