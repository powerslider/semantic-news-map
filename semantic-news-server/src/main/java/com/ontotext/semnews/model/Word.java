package com.ontotext.semnews.model;

/**
 * Model class representing data used for word cloud visualisation.
 *
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 11-May-2016
 */
public class Word {

    private String text;

    private double size;

    private String entityUri;


    public Word() {
        super();
    }

    public Word(String text, int size, String entityUri) {
        this.text = text;
        this.size = size;
        this.entityUri = entityUri;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getEntityUri() {
        return entityUri;
    }

    public void setEntityUri(String entityUri) {
        this.entityUri = entityUri;
    }
}
