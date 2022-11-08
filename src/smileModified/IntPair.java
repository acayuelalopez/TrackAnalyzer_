package smileModified;


/** A pair of integer. */
public class IntPair {
    /** The first integer. */
    public final int i;
    /** The second integer. */
    public final int j;

    /**
     * Constructor.
     * @param i the first integer.
     * @param j the second integer.
     */
    public IntPair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public int hashCode() {
        return i * 31 + j;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", i, j);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntPair) {
            IntPair p = (IntPair) o;
            return i == p.i && j == p.j;
        }

        return false;
    }
}