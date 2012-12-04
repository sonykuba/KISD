package com.pwr.kisd;

public class MatrixMath {

	public static Matrix transpose(Matrix input) {
		final boolean inverseMatrix[][] = new boolean[input.getCols()][input.getRows()];
		for (int r = 0; r < input.getRows(); r++) {
			for (int c = 0; c < input.getCols(); c++) {
				inverseMatrix[c][r] = input.get(r, c);
			}
		}
		return new Matrix(inverseMatrix);

	}

	public static Matrix identity(int size) {
		final Matrix result = new Matrix(size, size);
		for (int i = 0; i < size; i++) {
			result.set(i, i, true);
		}
		return result;
	}

	public static Matrix multiply(Matrix A, Matrix B) throws Exception {
		if (A.getCols() != B.getRows())
			if (A.getCols() == B.getCols())
				return multiply(A, transpose(B));
			else if (A.getCols() != B.getRows() && A.getCols() != B.getCols() && A.getRows() != B.getCols() && A.getRows() != A.getRows())
				throw new Exception();
			else
				return multiply(transpose(A), B);
		Matrix C = new Matrix(A.getRows(), B.getCols());
		for (int i = 0; i < C.getRows(); i++)
			for (int j = 0; j < C.getCols(); j++) {
				boolean out = false;
				for (int k = 0; k < A.getCols(); k++)
					if (B.get(k, j)) {
						out = MatrixMath.BooleanProduct(B.get(k, j), A.get(i, k));
					}
				C.set(i, j, out);
			}
		return C;
	}

	public static Matrix multiplyMatrixCells(Matrix a, Matrix b) {
		final boolean result[][] = new boolean[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) || b.get(row, col);
			}
		}
		return new Matrix(result);
	}

	public static Matrix multiplyByConstant(Matrix a, boolean b) {
		final boolean result[][] = new boolean[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = BooleanProduct(a.get(row, col), b);
			}
		}
		return new Matrix(result);
	}

	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(final boolean b[]) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.length - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}

	public static boolean BooleanProduct(boolean one, boolean two) {
		if (one && !two || !one && two)
			return true;
		else
			return false;
	}

	public static boolean BooleanSubstract(boolean one, boolean two) {
		if (one && !two || !one && !two)
			return true;
		else
			return false;
	}

	public static boolean BooleanAdd(boolean one, boolean two) {
		if (one && !two || !one && two)
			return true;
		else
			return false;
	}

	public static Matrix MatrixBooleanSubstract(Matrix a, Matrix b) {
		for (int i = 0; i < a.getCols(); i++)
			for (int l = 0; l < a.getRows(); l++)
				a.set(l, i, BooleanSubstract(a.get(l, i), b.get(l, i)));
		return a;
	}

	public static Matrix MatrixBooleanAdd(Matrix a, Matrix b) {
		for (int i = 0; i < a.getCols(); i++)
			for (int l = 0; l < a.getRows(); l++)
				a.set(l, i, BooleanAdd(a.get(l, i), b.get(l, i)));
		return a;
	}

	public static Matrix inverse(Matrix matrix) throws Exception {
		return multiplyByConstant(transpose(MatrixMath.cofactor(matrix)), MatrixMath.determinant(matrix));
	}

	public static Matrix cofactor(Matrix matrix) throws Exception {
		Matrix mat = new Matrix(matrix.getRows(), matrix.getCols());
		for (int i = 0; i < matrix.getRows(); i++) {
			for (int j = 0; j < matrix.getCols(); j++) {
				mat.set(i, j, BooleanProduct(BooleanProduct(i % 2 == 0 ? true : false, j % 2 == 0 ? true : false), MatrixMath.determinant(Matrix.createSubMatrix(matrix, i, j))));
			}
		}
		return mat;
	}

	public static boolean determinant(Matrix matrix) throws Exception {
		if (matrix.getCols() != matrix.getRows())
			throw new Exception("matrix need to be square.");

		if (matrix.getRows() == 2) {
			return BooleanSubstract(BooleanProduct(matrix.get(0, 0), matrix.get(1, 1)), BooleanProduct(matrix.get(0, 1), matrix.get(1, 0)));
		}
		boolean sum = false;
		for (int i = 0; i < matrix.getCols(); i++) {
			sum = BooleanAdd(sum, BooleanProduct(i % 2 == 0 ? true : false, BooleanProduct(matrix.get(0, i), determinant(Matrix.createSubMatrix(matrix, 0, i)))));
		}
		return sum;
	}

}
