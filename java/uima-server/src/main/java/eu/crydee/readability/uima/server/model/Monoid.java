package eu.crydee.readability.uima.server.model;

import java.util.Comparator;

public interface Monoid extends Comparator<Double> {

    double getUnit();

    double mul(double a, double b);
}
