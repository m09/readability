package eu.crydee.readability.uima.model;

import java.util.Comparator;

public interface Weight {

    double getUnit();

    double getZero();

    double add(double a, double b);

    double mul(double a, double b);
    
    Comparator<Double> comparator();
}
