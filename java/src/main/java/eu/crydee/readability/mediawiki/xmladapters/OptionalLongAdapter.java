package eu.crydee.readability.mediawiki.xmladapters;

import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OptionalLongAdapter extends XmlAdapter<String, Optional<Long>>{

    @Override
    public Optional<Long> unmarshal(String v) throws Exception {
        if (v != null) {
            return Optional.of(Long.parseLong(v));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String marshal(Optional<Long> v) throws Exception {
        if (v.isPresent()) {
            return String.valueOf(v.get());
        } else {
            return null;
        }
    }
    
}
