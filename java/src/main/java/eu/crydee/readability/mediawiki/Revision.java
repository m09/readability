package eu.crydee.readability.mediawiki;

import eu.crydee.readability.mediawiki.xmladapters.BooleanAdapter;
import eu.crydee.readability.mediawiki.xmladapters.ZonedDateTimeAdapter;
import eu.crydee.readability.mediawiki.xmladapters.OptionalStringAdapter;
import eu.crydee.readability.mediawiki.xmladapters.OptionalLongAdapter;
import java.time.ZonedDateTime;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Revision {

    @XmlAttribute
    @XmlJavaTypeAdapter(OptionalLongAdapter.class)
    final private Optional<Long> parentId;
    @XmlAttribute
    final private long id;
    @XmlAttribute
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    
    final private ZonedDateTime timeStamp;
    @XmlAttribute
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    final private Boolean minor;
    @XmlAttribute
    @XmlJavaTypeAdapter(OptionalStringAdapter.class)
    final private Optional<String> comment;
    @XmlTransient
    final private String text;

    public Revision(
            Optional<Long> parentId,
            long id,
            ZonedDateTime timeStamp,
            boolean minor,
            Optional<String> comment,
            String text) {
        this.parentId = parentId;
        this.id = id;
        this.timeStamp = timeStamp;
        this.minor = minor;
        this.comment = comment;
        this.text = text;
    }

    public Optional<Long> getParentId() {
        return parentId;
    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public Boolean isMinor() {
        return minor;
    }

    public Optional<String> getComment() {
        return comment;
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Revision other = (Revision) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return " {comment: " + comment.orElse(null) + ", "
                + "id: " + id + ", "
                + "parentId: " + parentId.orElse(null) + ", "
                + "timestamp: " + timeStamp + ", "
                + "minor: " + minor + ", "
                + "minor: " + minor + "}";
    }
}
