package eu.crydee.readability;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;

public class Diff {

    private static final Logger logger = Logger.getLogger(
            Diff.class.getCanonicalName());

    final private RawText sourceRaw, targetRaw;
    private List<LineEdit> lineEdits = null;
    private List<WordEdit> wordEdits = null;

    public Diff(String source, String target) {
        this.sourceRaw
                = new RawText(source.getBytes(StandardCharsets.UTF_8));
        this.targetRaw
                = new RawText(target.getBytes(StandardCharsets.UTF_8));
    }

    public List<LineEdit> getLineEdits() {
        if (lineEdits == null) {
            computeLineEdits();
        }
        return Collections.unmodifiableList(lineEdits);
    }

    private void computeLineEdits() {
        lineEdits = new ArrayList<>();
        EditList edits = DiffUtils.diffLines(sourceRaw, targetRaw);
        for (Edit edit : edits) {
            if (edit.getType().equals(Edit.Type.REPLACE)) {
                lineEdits.add(new LineEdit(
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

    public List<WordEdit> getWordEdits() {
        if (wordEdits == null) {
            computeWordEdits();
        }
        return Collections.unmodifiableList(wordEdits);
    }

    private void computeWordEdits() {
        if (lineEdits == null) {
            computeLineEdits();
        }
        wordEdits = new ArrayList<>();
        for (LineEdit lineEdit : lineEdits) {
            String A = StringUtils.join(lineEdit.getTextA().split(" "), "\n")
                    + "\n",
                    B = StringUtils.join(lineEdit.getTextB().split(" "), "\n")
                    + "\n";
            RawText sourceRawWord = new RawText(A.getBytes(
                    StandardCharsets.UTF_8)),
                    targetRawWord = new RawText(B.getBytes(
                                    StandardCharsets.UTF_8));
            EditList edits = DiffUtils.diffLines(
                    sourceRawWord,
                    targetRawWord);
            for (Edit edit : edits) {
                if (edit.getType().equals(Edit.Type.REPLACE)
                        && edit.getLengthA() >= edit.getLengthB()) {
                    wordEdits.add(new WordEdit(
                            lineEdit,
                            edit.getBeginA(),
                            edit.getEndA(),
                            edit.getBeginB(),
                            edit.getEndB(),
                            sourceRawWord.getString(
                                    edit.getBeginA(),
                                    edit.getEndA(),
                                    true),
                            targetRawWord.getString(
                                    edit.getBeginB(),
                                    edit.getEndB(),
                                    true)));
                }
            }
        }
    }

    public String getLinePatch() {
        if (lineEdits == null) {
            computeLineEdits();
        }
        return getPatch(lineEdits);
    }

    public String getWordsPatch() {
        if (wordEdits == null) {
            computeWordEdits();
        }
        return getPatch(wordEdits);
    }

    private String getPatch(List<? extends eu.crydee.readability.Edit> edits) {
        StringBuilder sb = new StringBuilder();
        for (eu.crydee.readability.Edit edit : edits) {
            sb.append("- ")
                    .append(edit.getTextA())
                    .append("\n+ ")
                    .append(edit.getTextB())
                    .append("\n\n");
        }
        return sb.toString();
    }
}
