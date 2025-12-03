package util;

import exceptions.NotASquareMatrixException;
import model.Matrix;

public final class MatrixUtilities {
    private MatrixUtilities() {}

    public static Matrix floydWarshall(Matrix adj) {
        if (!Validate.squareMatrix(adj)) {
            throw new NotASquareMatrixException("Adjacency matrix must be a square matrix");
        }

        int m = adj.rows();
        int[][] entries = adj.entries();
        int[][] copyOfEntries = new int[m][m];

        for (int i = 0; i < m; i++) {
            System.arraycopy(entries[i], 0, copyOfEntries[i], 0, m);
        }

        for (int k = 0; k < m; k++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    if (copyOfEntries[i][k] == 1 && copyOfEntries[k][j] == 1) {
                        copyOfEntries[i][j] = 1;
                    }
                }
            }
        }

        return new Matrix(copyOfEntries, adj.rowLabels(), adj.columnLabels());
    }
}
