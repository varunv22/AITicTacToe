public class Tiles {
    private Elements elem;
    private int row;
    private int col;

    public Tiles(int row, int col, Elements elem) {
        this.row = row;
        this.col = col;
        this.elem = elem;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Tiles))
            return false;
        Tiles s = (Tiles) o;
        if (get_elem().equals(s.get_elem()))
            return true;
        return false;
    }

    public String toString() {
        return "[row: " + row + ", col: " + col + "]";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Elements get_elem() {
        return elem;
    }

    public void set_elem(Elements elem) {
        this.elem = elem;
    }
}