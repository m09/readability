package eu.crydee.readability.uima.server.model;

public class ProbaWeight implements Weight {

    @Override
    public double getUnit() {
        return 1d;
    }

    @Override
    public double getZero() {
        return 0d;
    }

    @Override
    public double add(double a, double b) {
        return a + b;
    }

    @Override
    public double mul(double a, double b) {
        return a * b;
    }

    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}
