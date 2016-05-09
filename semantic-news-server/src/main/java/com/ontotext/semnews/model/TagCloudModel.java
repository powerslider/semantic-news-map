package com.ontotext.semnews.model;

/**
 * Created by Boyan on 11-Mar-16.
 */
public class TagCloudModel {
    private String text;
    private int weight;
    private String link;

    public TagCloudModel() {

    }

    public TagCloudModel(String text, int weight, String link) {
        this.text = text;
        this.weight = weight;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
