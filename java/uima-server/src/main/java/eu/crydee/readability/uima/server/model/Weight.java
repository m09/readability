package eu.crydee.readability.uima.server.model;

import java.util.Comparator;

public interface Weight extends Comparator<Double> {

    double getUnit();

    double getZero();

    double add(double a, double b);

    double mul(double a, double b);
}