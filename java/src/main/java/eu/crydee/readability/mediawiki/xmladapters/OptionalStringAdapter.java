package eu.crydee.readability.mediawiki.xmladapters;

import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OptionalStringAdapter extends XmlAdapter<String, Optional<String>>{

    @Override
    public Optional<String> unmarshal(String v) throws Exception {
        if (v != null) {
            return Optional.of(v);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String marshal(Optional<String> v) throws Exception {
        if (v.isPresent()) {
            return v.get();
        } else {
            return null;
        }
    }
    
}
