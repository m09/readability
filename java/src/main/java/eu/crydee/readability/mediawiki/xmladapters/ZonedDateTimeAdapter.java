package eu.crydee.readability.mediawiki.xmladapters;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime>{

    @Override
    public ZonedDateTime unmarshal(String v) throws Exception {
        return ZonedDateTime.parse(v);
    }

    @Override
    public String marshal(ZonedDateTime dateTime) throws Exception {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
