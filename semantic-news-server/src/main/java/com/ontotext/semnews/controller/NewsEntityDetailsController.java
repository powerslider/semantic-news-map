package com.ontotext.semnews.controller;

import com.google.common.collect.ImmutableMap;
import com.ontotext.semnews.model.NewsEntity;
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

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 11-May-2016
 */
@RestController
public class NewsEntityDetailsController {

    @Autowired
    private SparqlService sparqlService;

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public Map<String, List<NewsEntity>> getDetailView(@RequestParam(value = "uri") String entitiUri,
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

}
