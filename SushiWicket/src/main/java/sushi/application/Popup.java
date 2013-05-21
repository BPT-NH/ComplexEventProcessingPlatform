package sushi.application;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;

public class Popup extends Panel
{

    public Popup(String id, String messageText) {
    	super(id);
        add(new MultiLineLabel("helpText", messageText));    
    }
}
