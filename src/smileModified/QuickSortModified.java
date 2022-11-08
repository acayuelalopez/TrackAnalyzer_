package smileModified;



import java.util.Comparator;

public class QuickSortModified {
    /** Private constructor to prevent instance creation. */
    private QuickSortModified() {

    }

    private static final int M = 7;
    private static final int NSTACK = 64;

    /**
     * Sorts the specified array into ascending numerical order.
     * @param x the array.
     * @return the original index of elements after sorting in range [0, n).
     */
    public static int[] sort(int[] x) {
        int[] order = new int[x.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        sort(x, order);
        return order;
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(int[] x, int[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(int[] x, int[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k, a, b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(int[] x, double[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(int[] x, double[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k, a;
        double b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(int[] x, Object[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(int[] x, Object[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k, a;
        Object b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Sorts the specified array into ascending numerical order.
     * @param x the array.
     * @return the original index of elements after sorting in range [0, n).
     */
    public static int[] sort(float[] x) {
        int[] order = new int[x.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        sort(x, order);
        return order;
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(float[] x, int[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(float[] x, int[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        float a;
        int b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(float[] x, float[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(float[] x, float[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        float a, b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(float[] x, Object[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(float[] x, Object[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        float a;
        Object b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Sorts the specified array into ascending numerical order.
     * @return the original index of elements after sorting in range [0, n).
     * @param x the array to SortModified.
     */
    public static int[] sort(double[] x) {
        int[] order = new int[x.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        sort(x, order);
        return order;
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(double[] x, int[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(double[] x, int[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        double a;
        int b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(double[] x, double[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(double[] x, double[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        double a, b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     */
    public static void sort(double[] x, Object[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     */
    public static void sort(double[] x, Object[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        double a;
        Object b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i] <= a) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l] > x[ir]) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1] > x[ir]) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l] > x[l + 1]) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i] < a);
                    do {
                        j--;
                    } while (x[j] > a);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Sorts the specified array into ascending order.
     * @return the original index of elements after sorting in range [0, n).
     * @param x the array to SortModified.
     * @param <T> the data type of array elements.
     */
    public static <T extends Comparable<? super T>>  int[] sort(T[] x) {
        int[] order = new int[x.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        sort(x, order);
        return order;
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param <T> the data type of array elements.
     */
    public static <T extends Comparable<? super T>>  void sort(T[] x, int[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     * @param <T> the data type of array elements.
     */
    public static <T extends Comparable<? super T>>  void sort(T[] x, int[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        T a;
        int b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i].compareTo(a) <= 0) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l].compareTo(x[ir]) > 0) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1].compareTo(x[ir]) > 0) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l].compareTo(x[l + 1]) > 0) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i].compareTo(a) < 0);
                    do {
                        j--;
                    } while (x[j].compareTo(a) > 0);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     * @param comparator the comparator.
     * @param <T> the data type of array elements.
     */
    public static <T>  void sort(T[] x, int[] y, int n, Comparator<T> comparator) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        T a;
        int b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (comparator.compare(x[i], a) <= 0) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (comparator.compare(x[l], x[ir]) > 0) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (comparator.compare(x[l + 1], x[ir]) > 0) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (comparator.compare(x[l], x[l + 1]) > 0) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (comparator.compare(x[i], a) < 0);
                    do {
                        j--;
                    } while (comparator.compare(x[j], a) > 0);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }

    /**
     * Besides sorting the array x, the array y will be also
     * rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param <T> the data type of array elements.
     */
    public static <T extends Comparable<? super T>>  void sort(T[] x, Object[] y) {
        sort(x, y, x.length);
    }

    /**
     * This is an efficient implementation Quick Sort algorithm without
     * recursive. Besides sorting the first n elements of array x, the first
     * n elements of array y will be also rearranged as the same order of x.
     * @param x the array to SortModified.
     * @param y the associate array.
     * @param n the first n elements to SortModified.
     * @param <T> the data type of array elements.
     */
    public static <T extends Comparable<? super T>>  void sort(T[] x, Object[] y, int n) {
        int jstack = -1;
        int l = 0;
        int[] istack = new int[NSTACK];
        int ir = n - 1;

        int i, j, k;
        T a;
        Object b;
        for (;;) {
            if (ir - l < M) {
                for (j = l + 1; j <= ir; j++) {
                    a = x[j];
                    b = y[j];
                    for (i = j - 1; i >= l; i--) {
                        if (x[i].compareTo(a) <= 0) {
                            break;
                        }
                        x[i + 1] = x[i];
                        y[i + 1] = y[i];
                    }
                    x[i + 1] = a;
                    y[i + 1] = b;
                }
                if (jstack < 0) {
                    break;
                }
                ir = istack[jstack--];
                l = istack[jstack--];
            } else {
                k = (l + ir) >> 1;
                SortModified.swap(x, k, l + 1);
                SortModified.swap(y, k, l + 1);
                if (x[l].compareTo(x[ir]) > 0) {
                    SortModified.swap(x, l, ir);
                    SortModified.swap(y, l, ir);
                }
                if (x[l + 1].compareTo(x[ir]) > 0) {
                    SortModified.swap(x, l + 1, ir);
                    SortModified.swap(y, l + 1, ir);
                }
                if (x[l].compareTo(x[l + 1]) > 0) {
                    SortModified.swap(x, l, l + 1);
                    SortModified.swap(y, l, l + 1);
                }
                i = l + 1;
                j = ir;
                a = x[l + 1];
                b = y[l + 1];
                for (;;) {
                    do {
                        i++;
                    } while (x[i].compareTo(a) < 0);
                    do {
                        j--;
                    } while (x[j].compareTo(a) > 0);
                    if (j < i) {
                        break;
                    }
                    SortModified.swap(x, i, j);
                    SortModified.swap(y, i, j);
                }
                x[l + 1] = x[j];
                x[j] = a;
                y[l + 1] = y[j];
                y[j] = b;
                jstack += 2;

                if (jstack >= NSTACK) {
                    throw new IllegalStateException("NSTACK too small in SortModified.");
                }

                if (ir - i + 1 >= j - l) {
                    istack[jstack] = ir;
                    istack[jstack - 1] = i;
                    ir = j - 1;
                } else {
                    istack[jstack] = j - 1;
                    istack[jstack - 1] = l;
                    l = i;
                }
            }
        }
    }
}