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
         <votes>
             <vote>
                 <score>10</score>
                 <image_id>7j0</image_id>
                 <sub_id>5555</sub_id>
                 <action>update</action>
             </vote>
             <vote>
                ...
             </vote>
            ...
         </votes>
     </data>
 </response>
 */
@Root(name = "response")
public class VoteResponse {

    @Element
    private Data data;

    private static class Data{

        @ElementList
        private List<Vote> votes;

    }

    public Vote getvote(int index){
        return data.votes.get(index);
    }

    public List<Vote> getvoteList() {return data.votes;}
}

