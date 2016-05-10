package com.ontotext.semnews.model;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 11-May-2016
 */
public class Word {

    private String text;
    private int weight;
    private String detailsUrl;

    public Word() {
        super();
    }

    public Word(String text, int weight, String detailsUrl) {
        this.text = text;
        this.weight = weight;
        this.detailsUrl = detailsUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
