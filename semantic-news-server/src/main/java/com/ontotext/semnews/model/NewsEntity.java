package com.ontotext.semnews.model;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 11-May-2016
 */
public class NewsEntity {

    private String title;
    private String date;
    private String uriLink;
    private String entityRelevance;
    private String relEntity;
    private String internalEntity;

    public String getRelEntity() {
        return relEntity;
    }

    public void setRelEntity(String relEntity) {
        this.relEntity = relEntity;
    }

    public String getInternalEntity() {
        return internalEntity;
    }

    public void setInternalEntity(String internalEntity) {
        this.internalEntity = internalEntity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUriLink() {
        return uriLink;
    }

    public void setUriLink(String uriLink) {
        this.uriLink = uriLink;
    }

    public String getEntityRelevance() {
        return entityRelevance;
    }

    public void setEntityRelevance(String entityRelevance) {
        this.entityRelevance = entityRelevance;
    }
}
