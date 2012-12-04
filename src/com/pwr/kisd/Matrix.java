package com.pwr.kisd;

public class Matrix {
	private final int rows; // number of rows
	private final int columns; // number of columns
	private final boolean[][] data; // M-by-N array
	private int H = 0;

	// create M-by-N matrix of 0's
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new boolean[rows][columns];
	}

	public Matrix(boolean[][] inverseMatrix) {
		this.rows = inverseMatrix.length;
		this.columns = inverseMatrix[0].length;
		data = inverseMatrix;
	}

	public Matrix(boolean[] codeWord) {
		this.rows = 1;
		this.columns = codeWord.length;
		data = new boolean[rows][columns];
		for (int i = 0; i < columns; i++)
			data[0][i] = codeWord[i];
	}

	public Matrix(Matrix codeWord) {
		this.rows = codeWord.getRows();
		this.columns = codeWord.getCols();
		data = new boolean[rows][columns];
		for (int i = 0; i < rows; i++)
			for (int l = 0; l < columns; l++)
				data[i][l] = codeWord.get(i, l);
	}

	public int getRows() {
		return rows;
	}

	public Matrix getCol(int col) {
		Matrix result = new Matrix(columns, 1);
		for (int i = 0; i < columns; i++)
			result.data[i][0] = data[i][col];
		return result;
	}

	public boolean isTheSame(Matrix matrix) {
		for (int i = 0; i < columns; i++)
			for (int l = 0; l < rows; l++) {
				if (get(l, i) != matrix.get(l, i))
					return false;
			}
		return true;
	}

	public int getCols() {
		return columns;
	}

	public void set(final int row, final int col, final boolean value) {
		data[row][col] = value;
	}

	public void flip(final int row, final int col) {
		data[row][col] = !data[row][col];
	}

	public boolean get(final int row, final int col) {
		return data[row][col];
	}

	public boolean[] toPackedArray() {
		boolean[] result = new boolean[rows * columns];
		for (int i = 0; i < columns; i++)
			for (int l = 0; l < rows; l++) {
				result[l * columns + i] = data[l][i];
			}
		return result;
	}

	public void show() {
		System.out.println("");
		for (int i = 0; i < rows; i++) {
			String temp = "";
			for (int l = 0; l < columns; l++)
				temp += data[i][l] + ", ";
			System.out.println(temp);
		}
	}

	public String toString() {
		String temp = "";
		for (int i = 0; i < rows; i++) {
			for (int l = 0; l < columns; l++)
				temp += data[i][l] + " ";
			temp += " \n ";
		}
		return temp;
	}

	public void randomize() {
		int onesCountPerRow = (int) (rows * 0.5);
		for (int l = 0; l < columns; l++) {
			int actualOnesNumber = 0;
			do {
				int i = (int) (Math.random() * rows);
				if (!data[i][l]) {
					data[i][l] = true;
					actualOnesNumber++;
				}
			} while (onesCountPerRow != actualOnesNumber);
		}
	}

	public int getOnesCount(int row) {
		int sum = 0;
		for (int i = 0; i < columns; i++)
			if (data[row][i])
				sum++;
		return sum;
	}

	public void sort() {
		int zerosAtPosition = 1;
		for (int k = 0; k < columns; k++) {
			int tempColumnNumber = -1;
			for (int i = 0; i < columns; i++) {
				int zerosCount = 0;
				for (int l = 0; l < rows; l++) {
					if (data[l][i]) {
						if (zerosAtPosition - 1 == zerosCount) {
							tempColumnNumber = i;
						}
						break;
					} else {
						zerosCount++;
					}
				}
				if (tempColumnNumber == i) {
					boolean[][] tempColumn = new boolean[data.length][1];
					for (int j = 0; j < data.length; j++) {
						tempColumn[j][0] = data[j][i];
					}
					for (int j = tempColumnNumber; j < columns - 1; j++)
						for (int l = 0; l < rows; l++) {
							data[l][j] = data[l][j + 1];
						}
					for (int j = 0; j < data.length; j++) {
						data[j][data[0].length - 1] = tempColumn[j][0];
					}
					zerosAtPosition++;
				}
			}
		}
	}

	public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
		Matrix mat = new Matrix(matrix.getRows() - 1, matrix.getCols() - 1);
		int r = -1;
		for (int i = 0; i < matrix.getRows(); i++) {
			if (i == excluding_row)
				continue;
			r++;
			int c = -1;
			for (int j = 0; j < matrix.getCols(); j++) {
				if (j == excluding_col)
					continue;
				mat.set(r, ++c, matrix.get(i, j));
			}
		}
		return mat;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public static Matrix CreateMatrix(Matrix codeWord, Matrix p1, Matrix p2) {
		if (codeWord.getRows() != 1)
			codeWord = MatrixMath.transpose(codeWord);
		if (p1.getRows() != 1)
			p1 = MatrixMath.transpose(p1);
		if (p2.getRows() != 1)
			p2 = MatrixMath.transpose(p2);

		Matrix toReturn = new Matrix(1, codeWord.getCols() + p1.getCols() + p2.getCols());
		toReturn.put(codeWord, 0);
		toReturn.put(p1, codeWord.getCols());
		toReturn.put(p2, codeWord.getCols() + p1.getCols());
		return toReturn;
	}

	private void put(Matrix codeWord, int lenght) {
		for (int i = 0; i < codeWord.getCols(); i++)
			data[0][i + lenght] = codeWord.get(0, i);
	}

	public static Matrix getE(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getRows() - matrix.getH(), matrix.getH());
		for (int i = matrix.getCols() - mat.getCols(); i < matrix.getCols(); i++)
			for (int l = matrix.getRows() - mat.getRows(); l < matrix.getRows(); l++)
				mat.set(l + mat.getRows() - matrix.getRows(), i + mat.getCols() - matrix.getCols(), matrix.get(l, i));
		return mat;
	}

	public static Matrix getA(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getCols() - matrix.getRows(), matrix.getRows() - matrix.getH());
		for (int i = 0; i < mat.getCols(); i++)
			for (int l = 0; l < mat.getRows(); l++)
				mat.set(l, i, matrix.get(i, l));
		return mat;
	}

	public static Matrix getC(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getH(), matrix.getCols() - matrix.getRows());
		for (int i = 0; i < mat.getCols(); i++)
			for (int l = matrix.getRows() - mat.getRows(); l < matrix.getRows(); l++)
				mat.set(l - matrix.getRows() + mat.getRows(), i, matrix.get(l, i));
		return mat;
	}

	public static Matrix getT(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getRows() - matrix.getH(), matrix.getRows() - matrix.getH());
		for (int i = matrix.getCols() - mat.getCols(); i < matrix.getCols(); i++)
			for (int l = 0; l < mat.getRows(); l++)
				mat.set(l, i + mat.getCols() - matrix.getCols(), matrix.get(l, i));
		return mat;
	}

	public static Matrix getB(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getRows() - matrix.getH(), matrix.getH());
		for (int i = matrix.getCols() - matrix.getRows(); i < matrix.getCols() - matrix.getRows() + matrix.getH(); i++)
			for (int l = 0; l < matrix.getRows() - matrix.getH(); l++)
				mat.set(l, i - (matrix.getCols() - matrix.getRows()), matrix.get(l, i));
		return mat;
	}

	public static Matrix getD(Matrix matrix) {
		Matrix mat = new Matrix(matrix.getH(), matrix.getH());
		for (int i = matrix.getCols() - matrix.getRows(); i < matrix.getCols() - matrix.getRows() + matrix.getH(); i++)
			for (int l = matrix.getRows() - matrix.getH(); l < matrix.getRows(); l++)
				mat.set(l - (matrix.getRows() - matrix.getH()), i - (matrix.getCols() - matrix.getRows()), matrix.get(l, i));
		return mat;
	}

	public static boolean[] generateCodeWord(int length) {
		boolean[] codeword = new boolean[length];
		for (int i = 0; i < length; i++)
			codeword[i] = Math.random() > 0.5;
		return codeword;
	}

	public static int compareMatrixs(Matrix codeWord, Matrix countedCodeWord) {
		int errors = 0;
		for (int i = 0; i < codeWord.getCols(); i++)
			if (codeWord.get(0, i) != countedCodeWord.get(0, i))
				errors++;
		return errors;
	}

	public int getOneNumber(int row, int column) {
		int onesCount = 0;
		for (int i = 0; i < column; i++)
			if (data[row][i])
				onesCount++;
		return onesCount;
	}
}
