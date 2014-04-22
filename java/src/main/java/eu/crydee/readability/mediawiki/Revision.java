package eu.crydee.readability.mediawiki;

import java.time.ZonedDateTime;
import java.util.Optional;

public class Revision {

    final private Optional<Long> parentId;

    final private long id;

    final private ZonedDateTime timeStamp;

    final private Boolean minor;

    final private Optional<String> comment;

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
