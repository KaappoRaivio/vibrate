package e.kaappo.vibrate;

public enum Morse {
    DIT (300, "·"), DASH (900, "–"), INTRA_CHAR_PAUSE(300, " "), INTER_CHARACTER_PAUSE (900, " ");

    private long length;
    private String symbol;

    public long getLength () {
        return length;
    }

    public void setLength (long length) {
        this.length = length;
    }

    public String getSymbol () {
        return symbol;
    }

    Morse (long length, String symbol) {
        this.length = length;
        this.symbol = symbol;
    }

}
