package eu.crydee.readability.uima.server.model;

public class MaxMinWeight implements Weight {

    @Override
    public double getUnit() {
        return 0d;
    }

    @Override
    public double getZero() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double add(double a, double b) {
        return Math.max(a, b);
    }

    @Override
    public double mul(double a, double b) {
        return Math.min(a, b);
    }

    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }

    @Override
    public String toString() {
        return "MaxMin";
    }
}
