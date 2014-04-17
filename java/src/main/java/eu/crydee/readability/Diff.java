package eu.crydee.readability;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;

public class Diff {

    private static final Logger logger = Logger.getLogger(
            Diff.class.getCanonicalName());

    final private RawText sourceRaw, targetRaw;
    private List<Edit> edits = null;

    public Diff(String source, String target) {
        this.sourceRaw
                = new RawText(source.getBytes(StandardCharsets.UTF_8));
        this.targetRaw
                = new RawText(target.getBytes(StandardCharsets.UTF_8));
    }

    public List<Edit> getLineEdits() {
        if (edits == null) {
            computeLineEdits();
        }
        return Collections.unmodifiableList(edits);
    }

    private void computeLineEdits() {
        edits = new ArrayList<>();
        EditList editList = DiffUtils.diffLines(sourceRaw, targetRaw);
        for (org.eclipse.jgit.diff.Edit edit : editList) {
            if (edit.getType().equals(
                    org.eclipse.jgit.diff.Edit.Type.REPLACE)) {
                this.edits.add(new Edit(
                        edit.getBeginA(),
                        edit.getEndA(),
                        edit.getBeginB(),
                        edit.getEndB(),
                        sourceRaw.getString(
                                edit.getBeginA(),
                                edit.getEndA(),
                                true),
                        targetRaw.getString(
                                edit.getBeginB(),
                                edit.getEndB(),
                                true)));
            }
        }
    }

    public String getLinePatch() {
        if (edits == null) {
            computeLineEdits();
        }
        return getPatch(edits);
    }

    private String getPatch(List<Edit> edits) {
        StringBuilder sb = new StringBuilder();
        for (Edit edit : edits) {
            sb.append("- ")
                    .append(edit.getTextA())
                    .append("\n+ ")
                    .append(edit.getTextB())
                    .append("\n\n");
        }
        return sb.toString();
    }
}
