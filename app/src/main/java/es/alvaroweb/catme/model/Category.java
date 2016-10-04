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
public class Category {
    @Element
    private int id;

    @Element
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}