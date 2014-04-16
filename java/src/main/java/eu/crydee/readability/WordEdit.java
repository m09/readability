package eu.crydee.readability;

public class WordEdit implements Edit {

    final private LineEdit lineEdit;
    final private int beginA, endA, beginB, endB;
    final private String textA, textB;

    public WordEdit(LineEdit lineEdit,
            int beginA,
            int endA,
            int beginB,
            int endB,
            String textA,
            String textB) {
        this.lineEdit = lineEdit;
        this.beginA = beginA;
        this.endA = endA;
        this.beginB = beginB;
        this.endB = endB;
        this.textA = textA;
        this.textB = textB;
    }

    public LineEdit getLineEdit() {
        return lineEdit;
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
