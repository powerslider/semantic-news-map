package com.ontotext.semnews.service;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Boyan on 12-Mar-16.
 */
public class QueryService {

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

    public List<TagCloudModel> getWordCloudResutlts(String from, String hidden, String industry, boolean relativePop) {
        List<TagCloudModel> words = new ArrayList<>();
        if (hidden.equals("0")) {
            words = getMostFrequentEntities(from, industry, relativePop);
        } else if (hidden.equals("1")) {
            words = getHiddenChampions(from, industry, false);
        } else {
            words = getHiddenChampions(from, industry, true);
        }


        return words;
    }

    public List<TagCloudModel> getMostFrequentEntities(String from, String industry, boolean relativePop) {
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
        List<TagCloudModel> words = new ArrayList<>();

        try {
            while (result.hasNext()) {
                TagCloudModel tagCloudModel = new TagCloudModel();
                BindingSet bind = result.next();

                if (bind.getBinding("entity_label") != null) {
                    tagCloudModel.setText(bind.getValue("entity_label").stringValue());
                }
                if (bind.getBinding("relative_popularity") != null) {
                    tagCloudModel.setWeight(Integer.valueOf(bind.getValue("relative_popularity").stringValue()));
                }
                if (bind.getBinding("mentioned_lod_entity") != null) {
                    tagCloudModel.setLink("/details?uri=" + bind.getValue("mentioned_lod_entity").stringValue() +
                            "&from=" + from + "&industry=" + industry);
                }
                words.add(tagCloudModel);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }
        return  words;
    }

    public List<TagCloudModel> getHiddenChampions(String from, String industry, Boolean hiddenAndFrequent) {
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
        List<TagCloudModel> words = new ArrayList<>();

        try {
            while (result.hasNext()) {
                TagCloudModel tagCloudModel = new TagCloudModel();
                BindingSet bind = result.next();

                if (bind.getBinding("entity_name") != null) {
                    tagCloudModel.setText(bind.getValue("entity_name").stringValue());
                }
                if (bind.getBinding("relWeight") != null) {
                    tagCloudModel.setWeight(Integer.valueOf(bind.getValue("relWeight").stringValue()));
                }
                if (bind.getBinding("rel_entity") != null) {
                    tagCloudModel.setLink("/details?uri=" + bind.getValue("rel_entity").stringValue() +
                            "&from=" + from + "&industry=" + industry);
                }
                words.add(tagCloudModel);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return words;
    }

    public List<NewsEntityModel> getNewsMentioningEntitie(String from, String industry, String uri) {
        List<NewsEntityModel> newsEntityModels = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = newsMentoningEntity.replace("{date}", d);
        q = q.replace("{date1}", d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);
        q = q.replaceAll(Pattern.quote("{entity}"), uri);

        TupleQueryResult result = evaluateQuery(q);

        try {
            while (result.hasNext()) {
                NewsEntityModel newsEntityModel = new NewsEntityModel();
                BindingSet bind = result.next();

                if (bind.getBinding("news_title") != null) {
                    newsEntityModel.setTitle(bind.getValue("news_title").stringValue());
                }
                if (bind.getBinding("news_date") != null) {
                    newsEntityModel.setDate(bind.getValue("news_date").stringValue());
                }
                if (bind.getBinding("news") != null) {
                    newsEntityModel.setUriLink(bind.getValue("news").stringValue());
                }
                if (bind.getBinding("entity_relevance") != null) {
                    newsEntityModel.setEntityRelevance(bind.getValue("entity_relevance").stringValue());
                }
                newsEntityModels.add(newsEntityModel);

            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return newsEntityModels;
    }

    public List<NewsEntityModel> getNewsMentioningRelEntitie(String from, String industry, String uri) {
        List<NewsEntityModel> newsEntityModels = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = newsMentioningRelEntity.replace("{date}", d);
        q = q.replace("{date1}", d);
        q = q.replaceAll(Pattern.quote("{category}"), industry);
        q = q.replaceAll(Pattern.quote("{entity}"), uri);

        TupleQueryResult result = evaluateQuery(q);

        try {
            while (result.hasNext()) {
                NewsEntityModel newsEntityModel = new NewsEntityModel();
                BindingSet bind = result.next();

                if (bind.getBinding("title") != null) {
                    newsEntityModel.setTitle(bind.getValue("title").stringValue());
                }
                if (bind.getBinding("date") != null) {
                    newsEntityModel.setDate(bind.getValue("date").stringValue());
                }
                if (bind.getBinding("news") != null) {
                    newsEntityModel.setUriLink(bind.getValue("news").stringValue());
                }
                if (bind.getBinding("rel_entity") != null) {
                    newsEntityModel.setRelEntity(bind.getValue("rel_entity").stringValue());
                }
                if (bind.getBinding("intermed_entity") != null) {
                    newsEntityModel.setInternalEntity(bind.getValue("intermed_entity").stringValue());
                }
                if (bind.size() > 0) {
                    newsEntityModels.add(newsEntityModel);
                }


            }
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }

        return newsEntityModels;
    }



    public List<HeatMapModel> getHeatMap(String from) {
        List<HeatMapModel> heatMap = new ArrayList<>();
        String q = "";
        String d = parseDate(from);
        q = heatMapQuery.replace("{date}", d);
        q = q.replace("{date1}", d);

        TupleQueryResult result = evaluateQuery(q);
        try {
            while (result.hasNext()) {
                HeatMapModel heatMapModel = new HeatMapModel();
                BindingSet bind = result.next();

                if (bind.getBinding("label") != null) {
                    heatMapModel.setCountry(bind.getValue("label").stringValue());
                }
                if (bind.getBinding("mention") != null) {
                    heatMapModel.setFrequency(Integer.parseInt(bind.getValue("mention").stringValue()));
                }
                heatMap.add(heatMapModel);

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
