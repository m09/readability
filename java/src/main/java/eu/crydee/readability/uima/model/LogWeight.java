package eu.crydee.readability.uima.model;

import java.util.Comparator;

public class LogWeight implements Weight {

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
        double max, min;
        if (a > b) {
            max = a;
            min = b;
        } else {
            max = b;
            min = a;
        }
        return max + Math.log1p(Math.exp(min - max));
    }

    @Override
    public double mul(double a, double b) {
        return a + b;
    }

    @Override
    public Comparator<Double> comparator() {
        return (new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return Double.compare(o1, o2);
            }
        });
    }
}
