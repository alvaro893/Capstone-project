/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import static android.R.attr.action;

/*
 * TODO: Create JavaDoc
 */
@Root(name = "vote")
public class Vote {
    @Element
    private int score;

    @Element(name = "image_id")
    private String imageId;

    @Element(name = "sub_id")
    private String subId;

    @Element
    private String action;


    public int getScore() {
        return score;
    }

    public String getImageId() {
        return imageId;
    }

    public String getSubId() {
        return subId;
    }

    public String getAction() {
        return action;
    }
}
