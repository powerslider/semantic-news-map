package com.ontotext.semnews.service;

import com.codepoetics.protonpack.StreamUtils;
import com.ontotext.semnews.model.Word;
import com.ontotext.semnews.model.WorldHeatMap;
import com.ontotext.semnews.model.NewsEntity;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Boyan on 12-Mar-16.
 */
@Service
public class SemanticNewsMapService {

    @Value("${sesame.server}")
    private String sesameServer;

    @Value("${repository.id}")
    private String repositoryID;

    @Value("${word.cloud}")
    private String wordCloud;

    @Value("${word.hidden.champ}")
    private String hid;

    @Value("${news.by.country}")
    private String heatMapQuery;

    @Value("${word.hidden.frequent}")
    private String hidPopular;

    @Value("${most.popular.nonNorm}")
    private String relPopularity;

    @Value("${news.mentioning.entity}")
    private String newsMentoningEntity;

    @Value("${news.mentioning.rel.entities}")
    private String newsMentioningRelEntity;

    public Repository getRepository(){
        Repository repository = new HTTPRepository(sesameServer, repositoryID);
        try {
            repository.initialize();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return repository;
    }

    public TupleQueryResult evaluateQuery(String query) {
        Repository repository = getRepository();
        RepositoryConnection repositoryConnection = null;
        TupleQueryResult tupleQueryResult = null;
        try {
            repositoryConnection = repository.getConnection();
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            tupleQueryResult = tupleQuery.evaluate();
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            e.printStackTrace();
        }
        return tupleQueryResult;
    }

    public List<Word> getWordCloudResults(String from, String hidden, String industry, boolean relativePop) {
        List<Word> words;
        switch (hidden) {
            case "0":
                words = getMostFrequentEntities(from, industry, relativePop);
                break;
            case "1":
                words = getHiddenChampions(from, industry, false);
                break;
            default:
                words = getHiddenChampions(from, industry, true);
                break;
        }


        return words;
    }

    public List<Word> getMostFrequentEntities(Map<String, List<String>> entities, String from, String category) {
        Stream<String> labels = entities.get("entities_label").stream();
        Stream<String> weights = entities.get("relative_popularity").stream();
        Stream<String> mentionedEntities = entities.get("mentioned_lod_entity").stream();

        return zip2Words(from, category, labels, weights, mentionedEntities);
    }

    private List<Word> zip2Words(String from, String category, Stream<String> labels, Stream<String> weights, Stream<String> mentionedEntities) {
        return StreamUtils.zip(labels, weights, mentionedEntities,
                (label, weight, mentionedEntity) -> {
                    Word word = new Word();
                    word.setText(label);
                    word.setWeight(Integer.parseInt(weight));
                    word.setDetailsUrl("/details?uri=" + mentionedEntity +
                            "&from=" + from + "&category=" + category);
                    return word;
                })
                .collect(Collectors.toList());
    }

    public List<Word> getHiddenChampions(Map<String, List<String>> entities, String from, String category) {
        Stream<String> labels = entities.get("entities_name").stream();
        Stream<String> weights = entities.get("relWeight").stream();
        Stream<String> mentionedEntities = entities.get("rel_entity").stream();

        return zip2Words(from, category, labels, weights, mentionedEntities);
    }

    public List<NewsEntity> getNewsMentioningEntitie(String from, String industry, String uri) {
        List<NewsEntity> newsEntities = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = newsMentoningEntity.replace("{date}", d);
        q = q.replace("{date1}", d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);
        q = q.replaceAll(Pattern.quote("{entity}"), uri);

        TupleQueryResult result = evaluateQuery(q);

        try {
            while (result.hasNext()) {
                NewsEntity newsEntity = new NewsEntity();
                BindingSet bind = result.next();

                if (bind.getBinding("news_title") != null) {
                    newsEntity.setTitle(bind.getValue("news_title").stringValue());
                }
                if (bind.getBinding("news_date") != null) {
                    newsEntity.setDate(bind.getValue("news_date").stringValue());
                }
                if (bind.getBinding("news") != null) {
                    newsEntity.setUriLink(bind.getValue("news").stringValue());
                }
                if (bind.getBinding("entity_relevance") != null) {
                    newsEntity.setEntityRelevance(bind.getValue("entity_relevance").stringValue());
                }
                newsEntities.add(newsEntity);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return newsEntities;
    }

    public List<NewsEntity> getNewsMentioningRelEntitie(String from, String industry, String uri) {
        List<NewsEntity> newsEntities = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = newsMentioningRelEntity.replace("{date}", d);
        q = q.replace("{date1}", d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);
        q = q.replaceAll(Pattern.quote("{entity}"), uri);

        TupleQueryResult result = evaluateQuery(q);

        try {
            while (result.hasNext()) {
                NewsEntity newsEntity = new NewsEntity();
                BindingSet bind = result.next();

                if (bind.getBinding("title") != null) {
                    newsEntity.setTitle(bind.getValue("title").stringValue());
                }
                if (bind.getBinding("date") != null) {
                    newsEntity.setDate(bind.getValue("date").stringValue());
                }
                if (bind.getBinding("news") != null) {
                    newsEntity.setUriLink(bind.getValue("news").stringValue());
                }
                if (bind.getBinding("rel_entity") != null) {
                    newsEntity.setRelEntity(bind.getValue("rel_entity").stringValue());
                }
                if (bind.getBinding("intermed_entity") != null) {
                    newsEntity.setInternalEntity(bind.getValue("intermed_entity").stringValue());
                }
                if (bind.size() > 0) {
                    newsEntities.add(newsEntity);
                }


            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return newsEntities;
    }



    public List<WorldHeatMap> getHeatMap(String from) {
        List<WorldHeatMap> heatMap = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = heatMapQuery.replace("{date}", d);
        q = q.replace("{date1}", d);

        TupleQueryResult result = evaluateQuery(q);
        try {
            while (result.hasNext()) {
                WorldHeatMap worldHeatMap = new WorldHeatMap();
                BindingSet bind = result.next();

                if (bind.getBinding("label") != null) {
                    worldHeatMap.setCountry(bind.getValue("label").stringValue());
                }
                if (bind.getBinding("mention") != null) {
                    worldHeatMap.setFrequency(Integer.parseInt(bind.getValue("mention").stringValue()));
                }
                heatMap.add(worldHeatMap);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return heatMap;
    }


    public String toIsoLocalDate(String dateString) {
        DateTimeFormatter fromFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate parsedDate = LocalDate.parse(dateString, fromFormat);
        return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

//    private String getAllCategories(){
//        WordCloudController tagCloudController = new WordCloudController();
//        Set<String> cat =  tagCloudController.getPubCategory();
//        String
//    }
    }
