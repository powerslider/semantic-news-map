package com.ontotext.semnews.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.ontotext.semnews.model.Word;
import com.ontotext.semnews.service.SemanticNewsMapService;
import com.ontotext.semnews.service.SparqlService;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class WordCloudController {

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


    @Autowired
    private SparqlService sparqlService;

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;


    //	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String printWelcome(ModelMap model) {
//		model.addAttribute("from", "");
//		model.addAttribute("catA", "All");
//		return "tagcloud";
//	}
//
    @RequestMapping(value = "/word-cloud", method = RequestMethod.GET)
    @ResponseBody
    public List<Word> getWordCloud(@RequestParam("from") String from,
                                   @RequestParam("hidden") String hidden,
                                   @RequestParam("category") String category,
                                   @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity) {

        sparqlService.new WithConnection<List<Word>>() {

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
                if (!Strings.isNullOrEmpty(hidden) || hidden.equals("false")) {
                    if(relPopularity) {
                        queryResults = executeWordCloudQuery("sparql-queries/mostPopularNonNormalized");
                        return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                    } else {
                        queryResults = executeWordCloudQuery("sparql-queries/wordCloud");
                        return semanticNewsMapService.getMostFrequentEntities(queryResults, from, category);
                    }
                } else if (hidden.equals("true")) {
                    queryResults = executeWordCloudQuery("sparql-queries/hiddenChampions");
                    return semanticNewsMapService.getHiddenChampions(queryRsults, from, category);
                } else {
                    queryResults = executeWordCloudQuery("sparql-queries/hiddenAndPopular");
                    return semanticNewsMapService.getHiddenChampions(queryResults, from, category);
                }

                return null;
            }
        }.run();

        return semanticNewsMapService.getWordCloudResults(from, hidden, category, relPopularity);
//	}
//
//	@RequestMapping(value = "/categories", method = RequestMethod.GET)
//	@ResponseBody
//	public Set<String> getCategories() {
//		return PUB_CATEGORIES;
//
//	}
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
}
