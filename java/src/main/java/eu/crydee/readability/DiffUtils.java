package eu.crydee.readability;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class DiffUtils {

    public static EditList diffLines(String a, String b) {
        return diffLines(new RawText(a.getBytes()), new RawText(b.getBytes()));
    }

    public static EditList diffLines(RawText a, RawText b) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);

        return da.diff(RawTextComparator.DEFAULT, a, b);
    }

    public static EditList diffWords(String a, String b) {
        String A = StringUtils.join(a.split(" "), "\n") + "\n",
                B = StringUtils.join(b.split(" "), "\n") + "\n";
        return diffLines(
                new RawText(A.getBytes()), new RawText(B.getBytes()));
    }
}
