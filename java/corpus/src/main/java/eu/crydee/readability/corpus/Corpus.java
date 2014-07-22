package eu.crydee.readability.corpus;

import java.net.URL;

/**
 * Class containing corpus metadata.
 *
 * @author mog
 */
public class Corpus {

    /**
     * Path to the corpus in the jar.
     */
    static public final URL url = Corpus.class.getClassLoader().getResource(
            "eu/crydee/readability/corpus/corpus.xml");
}
