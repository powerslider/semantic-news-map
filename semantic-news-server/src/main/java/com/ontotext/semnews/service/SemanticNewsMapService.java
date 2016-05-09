package com.ontotext.semnews.service;

import com.ontotext.semnews.model.WorldHeatMap;
import com.ontotext.semnews.model.NewsEntity;
import com.ontotext.semnews.model.WordCloud;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Boyan on 12-Mar-16.
 */
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

    public List<WordCloud> getWordCloudResutlts(String from, String hidden, String industry, boolean relativePop) {
        List<WordCloud> words = new ArrayList<>();
        if (hidden.equals("0")) {
            words = getMostFrequentEntities(from, industry, relativePop);
        } else if (hidden.equals("1")) {
            words = getHiddenChampions(from, industry, false);
        } else {
            words = getHiddenChampions(from, industry, true);
        }


        return words;
    }

    public List<WordCloud> getMostFrequentEntities(String from, String industry, boolean relativePop) {
        String q = "";
        String d = parseDate(from);
        if (relativePop) {
            q = wordCloud.replaceAll(Pattern.quote("{date}"), d);
        } else {
            q = relPopularity.replaceAll(Pattern.quote("{date}"), d);
        }

        q = q.replaceAll(Pattern.quote("{date1}"), d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);

        TupleQueryResult result = evaluateQuery(q);
        List<WordCloud> words = new ArrayList<>();

        try {
            while (result.hasNext()) {
                WordCloud wordCloud = new WordCloud();
                BindingSet bind = result.next();

                if (bind.getBinding("entity_label") != null) {
                    wordCloud.setText(bind.getValue("entity_label").stringValue());
                }
                if (bind.getBinding("relative_popularity") != null) {
                    wordCloud.setWeight(Integer.valueOf(bind.getValue("relative_popularity").stringValue()));
                }
                if (bind.getBinding("mentioned_lod_entity") != null) {
                    wordCloud.setLink("/details?uri=" + bind.getValue("mentioned_lod_entity").stringValue() +
                            "&from=" + from + "&industry=" + industry);
                }
                words.add(wordCloud);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }
        return  words;
    }

    public List<WordCloud> getHiddenChampions(String from, String industry, Boolean hiddenAndFrequent) {
        String q = "";
        String d = parseDate(from);
        if (!hiddenAndFrequent) {
            q = hid.replaceAll(Pattern.quote("{date}"), d);
        } else {
            q = hidPopular.replaceAll(Pattern.quote("{date}"), d);
        }

        q = q.replaceAll(Pattern.quote("{date1}"), d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);

        TupleQueryResult result = evaluateQuery(q);
        List<WordCloud> words = new ArrayList<>();

        try {
            while (result.hasNext()) {
                WordCloud wordCloud = new WordCloud();
                BindingSet bind = result.next();

                if (bind.getBinding("entity_name") != null) {
                    wordCloud.setText(bind.getValue("entity_name").stringValue());
                }
                if (bind.getBinding("relWeight") != null) {
                    wordCloud.setWeight(Integer.valueOf(bind.getValue("relWeight").stringValue()));
                }
                if (bind.getBinding("rel_entity") != null) {
                    wordCloud.setLink("/details?uri=" + bind.getValue("rel_entity").stringValue() +
                            "&from=" + from + "&industry=" + industry);
                }
                words.add(wordCloud);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return words;
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

    public String parseDate(String date) {
        String d[] = date.split("/");
        String n = d[2] + "-" + d[0] + "-" + d[1];
        return n;

    }

//    private String getAllCategories(){
//        TagCloudController tagCloudController = new TagCloudController();
//        Set<String> cat =  tagCloudController.getPubCategory();
//        String
//    }
    }
