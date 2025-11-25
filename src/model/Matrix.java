package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import exceptions.NotAMatrixException;
import util.Validate;

public final class Matrix {
    private final int m; // rows
    private final int n; // columns
    private final int[][] entries;
    private final List<String> rowLabels;
    private final List<String> columnLabels;

    /**
     * Creates a matrix from a two-dimensional array with row and column labels.
     * Throws a runtime exception if each row does not have the same number of columns or if array is empty.
     */
    public Matrix(int[][] entries, List<String> rowLabels, List<String> columnLabels) {
        if (!Validate.matrix(entries)) {
            throw new NotAMatrixException("Inconsistent number of columns or empty array");
        }

        this.m = entries.length;
        this.n = entries[0].length;
        
        this.entries = new int[m][n];

        for (int i = 0; i < m; i++) {
            System.arraycopy(entries[i], 0, this.entries[i], 0, n);
        }

        this.rowLabels = List.copyOf(rowLabels);
        this.columnLabels = List.copyOf(columnLabels);
    }

    /**
     * Creates an m x n matrix from a two-dimensional array with rows and column labels.
     * Entries not given in the array are filled with zeroes.
     */
    public Matrix(int m, int n, int[][] entries, List<String> rowLabels, List<String> columnLabels) {
        this.m = m;
        this.n = n;
        
        int[][] entriesWithZeroes = new int[m][n];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i < entries.length && j < entries[i].length) {
                    entriesWithZeroes[i][j] = entries[i][j];
                } else {
                    entriesWithZeroes[i][j] = 0;
                }
            }
        }

        this.entries = new int[m][n];

        for (int i = 0; i < m; i++) {
            System.arraycopy(entriesWithZeroes[i], 0, this.entries[i], 0, n);
        }

        this.rowLabels = List.copyOf(rowLabels);
        this.columnLabels = List.copyOf(columnLabels);
    }

    /**
     * Creates a matrix from a two-dimensional array.
     * Throws a runtime exception if each row does not have the same number of columns or if array is empty.
     */
    public Matrix(int[][] entries) {
        this(entries, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Creates an m x n matrix from a two-dimensional array.
     * Entries not given in the array are filled with zeroes.
     */
    public Matrix(int m, int n, int[][] entries) {
        this(m, n, entries, new ArrayList<>(), new ArrayList<>());
    }

    public int[][] entries() {
        return entries;
    }

    public int rows() {
        return m;
    }

    public int columns() {
        return n;
    }

    public List<String> rowLabels() {
        return rowLabels;
    }

    public List<String> columnLabels() {
        return columnLabels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;

        Matrix other = (Matrix) o;

        return this.m == other.m
            && this.n == other.n
            && Arrays.deepEquals(this.entries, other.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m, n, Arrays.deepHashCode(entries));
    }

    @Override
    public String toString() {
        // Convert entries to String
        String[][] strings = new String[m][n];
        int[] colWidths = new int[n];

        for (int j = 0; j < n; j++) {
            int max = columnLabels.size() > j ? columnLabels.get(j).length() : 0;
            for (int i = 0; i < m; i++) {
                strings[i][j] = String.valueOf(entries[i][j]);
                max = Math.max(max, strings[i][j].length());
            }
            colWidths[j] = max;
        }

        StringBuilder sb = new StringBuilder();

        // Compute width for row label column
        int rowLabelWidth = rowLabels.stream()
            .mapToInt(String::length)
            .max()
            .orElse(0);
        rowLabelWidth = Math.max(rowLabelWidth, 5); // optional minimum

        // Print column labels (with padding for row label column)
        sb.append(String.format("%" + rowLabelWidth + "s  ", "")); // empty space for row labels
        for (int j = 0; j < n; j++) {
            String label = columnLabels.size() > j ? columnLabels.get(j) : "";
            sb.append(String.format("%" + colWidths[j] + "s  ", label));
        }
        sb.append("\n");

        // Print each row
        for (int i = 0; i < m; i++) {
            String rowLabel = rowLabels.size() > i ? rowLabels.get(i) : "";
            sb.append(String.format("%-" + rowLabelWidth + "s  ", rowLabel));  // left-aligned row label
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%" + colWidths[j] + "s  ", strings[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
