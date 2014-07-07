package eu.crydee.readability.uima.server.model;

public class LogMonoid implements Monoid {

    @Override
    public double getUnit() {
        return 0d;
    }

    @Override
    public double mul(double a, double b) {
        return a + b;
    }

    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }

    @Override
    public String toString() {
        return "Log";
    }
}
