public class Elements {
    private String value;
    private int row;
    private int col;

    public Elements(String value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Elements))
            return false;
        Elements p = (Elements) o;
        if (getValue().equals(p.getValue()))
            return true;
        return false;
    }

    public String toString() {
        return "[" + value + "]";
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setValue(String value) {
        this.value = value;
        ;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getValue() {
        return value;
    }
}