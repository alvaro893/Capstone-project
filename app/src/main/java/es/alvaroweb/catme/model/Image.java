/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * TODO: Create JavaDoc
 */
@Root
public class Image {
    @Element
    private String url;

    @Element
    private int id;

    @Element(name = "source_url")
    private String sourceUrl;

    @Element
    private String created;

    @Element(name = "sub_id")
    private String userId;

    @Element
    private int score;

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getCreated() {
        return created;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
