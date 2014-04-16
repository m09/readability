package eu.crydee.readability;

public class LineEdit implements Edit {

    final private int beginA, endA, beginB, endB;
    final private String textA, textB;

    public LineEdit(int beginA,
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

    @Override
    public int getBeginA() {
        return beginA;
    }

    @Override
    public int getEndA() {
        return endA;
    }

    @Override
    public int getBeginB() {
        return beginB;
    }

    @Override
    public int getEndB() {
        return endB;
    }

    @Override
    public String getTextA() {
        return textA;
    }

    @Override
    public String getTextB() {
        return textB;
    }
}
