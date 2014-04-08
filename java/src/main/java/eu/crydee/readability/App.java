package eu.crydee.readability;

import org.apache.log4j.Logger;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

public class App {

    private static final Logger logger
            = Logger.getLogger(App.class.getCanonicalName());

    public static void main(String[] args) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);

        EditList editList = DiffUtils.diffWords("hai there", "hai hi there !");
        for (Edit edit : editList) {
            logger.info(edit.getType());
            logger.info(edit.toString());
            logger.info("begin A: " + edit.getBeginA());
            logger.info("end A:   " + edit.getEndA());
            logger.info("begin B: " + edit.getBeginB());
            logger.info("end B:   " + edit.getEndB());
        }
    }
}
