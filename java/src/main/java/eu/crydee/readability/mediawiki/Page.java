package eu.crydee.readability.mediawiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class Page {

    @XmlAttribute
    private String title;

    @XmlTransient
    private int ns = 0;

    @XmlAttribute
    private long id;

    @XmlTransient
    private boolean redirect = false;

    @XmlElement(name = "revision")
    private List<Revision> revisions = new ArrayList<>();

    public Page() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public List<Revision> getRevisions() {
        return Collections.unmodifiableList(revisions);
    }

    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }

    public void addRevision(Revision revision) {
        revisions.add(revision);
    }

    @Override
    public String toString() {
        return ("{title: " + title + ", "
                + "ns: " + ns + ", "
                + "id: " + id + ", "
                + "redirect: " + redirect + "}");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Page other = (Page) obj;
        return this.id == other.id;
    }
}
