package eu.crydee.readability.uima.core.res;

import java.io.PrintStream;

public interface Saveable {

    public void save(PrintStream ps) throws Exception;
}
