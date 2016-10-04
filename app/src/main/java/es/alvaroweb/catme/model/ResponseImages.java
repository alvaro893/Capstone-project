/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import static android.R.attr.id;

/*
 * TODO: Create JavaDoc
 */
@Root(name = "response")
public class ResponseImages {
    @Element
    private Data data;

    private static class Data{
        @Element
        private ImagesList images;

        private static class ImagesList {
            @ElementList
            private List<Image> imageList;
        }
    }

    public Image getImage(int index){
        return data.images.imageList.get(index);
    }
}
