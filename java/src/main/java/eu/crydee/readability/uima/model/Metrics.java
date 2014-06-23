package eu.crydee.readability.uima.model;

public class Metrics {

    private int count;
    private double scoreOcc,
            scoreLM,
            scoreLMW,
            scoreLMN,
            scoreLMWN;

    public Metrics(
            int count,
            double scoreOcc,
            double scoreLM,
            double scoreLMW,
            double scoreLMN,
            double scoreLMWN) {
        this.count = count;
        this.scoreOcc = scoreOcc;
        this.scoreLM = scoreLM;
        this.scoreLMW = scoreLMW;
        this.scoreLMN = scoreLMN;
        this.scoreLMWN = scoreLMWN;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
