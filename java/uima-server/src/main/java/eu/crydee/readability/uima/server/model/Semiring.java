package eu.crydee.readability.uima.server.model;

public final class Semiring {

    public static final Weight Log = new LogWeight(),
            MaxMin = new MaxMinWeight();

    public static Weight[] values() {
        return new Weight[]{Log, MaxMin};
    }
}