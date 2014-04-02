package eu.crydee.readability;

import org.apache.log4j.Logger;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class App {

    private static final Logger logger
            = Logger.getLogger(App.class.getCanonicalName());

    public static void main(String[] args) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);

        EditList editList = da.diff(RawTextComparator.DEFAULT,
                new RawText("hai there\nI'm fine.\n".getBytes()),
                new RawText("ha\nI'm fine.\nHow are you?\n".getBytes()));
        for (Edit edit : editList) {
            logger.info(edit.getType());
            logger.info(edit.toString());
            logger.info("begin A: " + edit.getBeginA());
            logger.info("end A:   " + edit.getEndA());
            logger.info("begin B: " + edit.getBeginB());
            logger.info("end B:   " + edit.getEndB());
        }
//        List<Pair<String, String>> pages = new ArrayList<>(Arrays.asList(
//                Pair.of("Evolution",
//                        "Evolution"),
//                Pair.of("Le_Spectre_de_la_Rose",
//                        "Le_Spectre_de_la_rose"),
//                Pair.of("Hanami",
//                        "Hanami"),
//                Pair.of("Geisha",
//                        "Geisha"),
//                Pair.of("Violin",
//                        "Violin"),
//                Pair.of("Ana_Ivanović",
//                        "Ana_Ivanović"),
//                Pair.of("Daniela_Hantuchová",
//                        "Daniela_Hantuchová"),
//                Pair.of("American_Airlines_Flight_11",
//                        "American_Airlines_Flight_11"),
//                Pair.of("Anna_Kournikova",
//                        "Anna_Kournikova"),
//                Pair.of("Jessica_Alba",
//                        "Jessica_Alba"),
//                Pair.of("Powderfinger",
//                        "Powderfinger"),
//                Pair.of("Baseball_uniform",
//                        "Baseball_uniform"),
//                Pair.of("Red_Hot_Chili_Peppers",
//                        "Red_Hot_Chili_Peppers"),
//                Pair.of("Gothic_architecture",
//                        "Gothic_architecture"),
//                Pair.of("Crich_Tramway_Village",
//                        "Crich_Tramway_Village"),
//                Pair.of("Ipswich_Town_F.C.",
//                        "Ipswich_Town_F.C.")));
//        for (Pair<String, String> page : pages) {
//            List<Revision> revisions = MediaWiki.getRevisions(
//                    page.getLeft(),
//                    Lang.ENGLISH);
//            logger.info(revisions.get(0).size
//                    + "B "
//                    + page.getLeft()
//                    + " (english):");
//            for (Revision rev : revisions) {
//                if (rev.comment.contains("simpl")) {
//                    logger.info("  " + rev.cleanedComment);
//                }
//            }
//            revisions = MediaWiki.getRevisions(
//                    page.getRight(),
//                    Lang.SIMPLE);
//            logger.info(revisions.get(0).size
//                    + "B "
//                    + page.getRight()
//                    + " (simple english):");
//            for (Revision rev : revisions) {
//                if (rev.comment.contains("simpl")) {
//                    logger.info("  " + rev.cleanedComment);
//                }
//            }
//        }
    }
}
