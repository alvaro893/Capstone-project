/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * TODO: Create JavaDoc
 */
@Root(strict = false)
public class Image {
    @Element
    private String url;

    @Element
    private String id;

    @Element(required = false)
    private String created;

    @Element(name = "sub_id", required = false)
    private String userId;

    @Element(required = false)
    private int score;

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
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
