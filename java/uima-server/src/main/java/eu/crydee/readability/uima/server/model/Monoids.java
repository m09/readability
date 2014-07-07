package eu.crydee.readability.uima.server.model;

public final class Monoids {

    public static final Monoid Log = new LogMonoid(),
            MaxMin = new MaxMinMonoid();

    public static Monoid[] values() {
        return new Monoid[]{Log, MaxMin};
    }
}
