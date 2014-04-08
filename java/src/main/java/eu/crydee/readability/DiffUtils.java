package eu.crydee.readability;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class DiffUtils {

    public static EditList diffLines(String a, String b) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);

        EditList editList = da.diff(RawTextComparator.DEFAULT,
                new RawText(a.getBytes()),
                new RawText(b.getBytes()));
        Object o = new Object();
        return editList;
    }

    public static EditList diffWords(String a, String b) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);
        String a2 = StringUtils.join(a.split(" "), "\n") + "\n",
                b2 = StringUtils.join(b.split(" "), "\n") + "\n";
        EditList editList = da.diff(RawTextComparator.DEFAULT,
                new RawText(a2.getBytes()),
                new RawText(b2.getBytes()));
        return editList;
    }
}
