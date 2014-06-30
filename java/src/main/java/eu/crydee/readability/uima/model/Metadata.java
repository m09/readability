package eu.crydee.readability.uima.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class Metadata {

    private final List<Pair<String, String>> contexts = new ArrayList<>();
    private double scoreOcc = 0d,
            scoreLM = 0d,
            scoreLMW = 0d,
            scoreLMN = 0d,
            scoreLMWN = 0d;

    public List<Pair<String, String>> getContexts() {
        return contexts;
    }

    public void addContext(String originalContext, String revisedContext) {
        this.contexts.add(Pair.of(originalContext, revisedContext));
    }

    public void addContexts(List<Pair<String, String>> contexts) {
        this.contexts.addAll(contexts);
    }

    public int getCount() {
        return contexts.size();
    }

    public double getScoreOcc() {
        return scoreOcc;
    }

    public void setScoreOcc(double scoreOcc) {
        this.scoreOcc = scoreOcc;
    }

    public double getScoreLM() {
        return scoreLM;
    }

    public void setScoreLM(double scoreLM) {
        this.scoreLM = scoreLM;
    }

    public double getScoreLMW() {
        return scoreLMW;
    }

    public void setScoreLMW(double scoreLMW) {
        this.scoreLMW = scoreLMW;
    }

    public double getScoreLMN() {
        return scoreLMN;
    }

    public void setScoreLMN(double scoreLMN) {
        this.scoreLMN = scoreLMN;
    }

    public double getScoreLMWN() {
        return scoreLMWN;
    }

    public void setScoreLMWN(double scoreLMWN) {
        this.scoreLMWN = scoreLMWN;
    }
}
