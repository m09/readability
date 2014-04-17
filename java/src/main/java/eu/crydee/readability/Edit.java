package eu.crydee.readability;

public class Edit {

    final private int beginA, endA, beginB, endB;
    final private String textA, textB;

    public Edit(int beginA,
            int endA,
            int beginB,
            int endB,
            String textA,
            String textB) {
        this.beginA = beginA;
        this.endA = endA;
        this.beginB = beginB;
        this.endB = endB;
        this.textA = textA;
        this.textB = textB;
    }

    public int getBeginA() {
        return beginA;
    }

    public int getEndA() {
        return endA;
    }

    public int getBeginB() {
        return beginB;
    }

    public int getEndB() {
        return endB;
    }

    public String getTextA() {
        return textA;
    }

    public String getTextB() {
        return textB;
    }
}
