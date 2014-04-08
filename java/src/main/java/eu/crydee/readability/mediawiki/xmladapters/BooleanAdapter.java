package eu.crydee.readability.mediawiki.xmladapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<String, Boolean>{

    @Override
    public Boolean unmarshal(String v) throws Exception {
        if (v != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String marshal(Boolean v) throws Exception {
        if (v != null && v) {
            return "true";
        } else {
            return null;
        }
    }
}
