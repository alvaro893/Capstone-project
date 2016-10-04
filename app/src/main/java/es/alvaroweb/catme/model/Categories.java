/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/*
 * TODO: Create JavaDoc
 */
@Root(name = "response")
public class Categories {

    @Element
    private Data data;

    private static class Data{
        @ElementList
        private List<Category> categories;
    }


    public List<Category> getCategories() {
        return data.categories;
    }
}
