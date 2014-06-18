package eu.crydee.readability.uima.model;

public class Metrics {

    private int count;
    private double score;

    public Metrics(Integer count, Double score) {
        this.count = count;
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
