/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * this class performs as an xml file like:
 *
 <response>
     <data>
        <images>
             <image>
             <id>1e3</id>
             <url>http://xxxxxxxxxxxxxxxxxxxx.jpg</url>
             <sub_id>5555</sub_id>
             <created>2016-10-02 21:06:57</created>
             <score>10</score>
             </image>
            ...
         </images>
     </data>
 </response>
 */
@Root(name = "response")
public class ResponseImages {
    @Element
    private Data data;

    private static class Data{
        @ElementList
        private List<Image> images;
    }

    public Image getImage(int index){
        return data.images.get(index);
    }

    public List<Image> getImageList() {return data.images;}
}
