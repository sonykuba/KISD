package com.pwr.kisd;

import java.math.BigInteger;

public class LDPC {

	Matrix generationMatrix;
	int columns;
	int rows;
	int H;

	public LDPC(int M, int N) throws Exception {
		generationMatrix = new Matrix(M, N);
		generationMatrix.randomize();
		generationMatrix.sort();
		generationMatrix.show();

		/*
		 * boolean[][] matrix = new boolean[][] { new boolean[] { false, true,
		 * false, true, true, false, false, true }, new boolean[] { true, true,
		 * true, false, false, true, false, false }, new boolean[] { false,
		 * false, true, false, false, true, true, true }, new boolean[] { true,
		 * false, false, true, true, false, true, false } };
		 */

		/*
		 * boolean[][] matrix = new boolean[][] { new boolean[] { true, true,
		 * true, true, false, false, true, true, false, false, false, false },
		 * new boolean[] { false, true, false, false, true, true, false, true,
		 * true, false, false, false }, new boolean[] { false, false, true,
		 * true, false, true, false, false, true, true, false, false }, new
		 * boolean[] { true, false, false, false, true, false, true, false,
		 * false, true, true, false }, new boolean[] { false, true, false, true,
		 * false, true, false, false, false, false, true, true }, new boolean[]
		 * { true, false, true, false, true, false, true, false, false, false,
		 * false, true } };
		 */
		/*
		 * boolean[][] matrix = new boolean[][] { new boolean[] { false, true,
		 * true, true, false, false, true, true, false, true, false, false },
		 * new boolean[] { false, false, false, false, false, false, false,
		 * true, false, false, true, false }, new boolean[] { false, false,
		 * true, true, false, false, false, false, false, true, true, true },
		 * new boolean[] { false, false, false, false, false, false, false,
		 * true, false, true, true, false }, new boolean[] { false, false,
		 * false, false, false, false, false, false, true, true, false, true },
		 * new boolean[] { false, true, false, true, true, false, false, false,
		 * true, false, true, true } };
		 */
		// generationMatrix = new Matrix(matrix);
		this.columns = generationMatrix.getCols();
		this.rows = generationMatrix.getRows();
		for (int i = 0; i < generationMatrix.getRows(); i++)
			if (generationMatrix.get(i, generationMatrix.getCols() - 1) == false)
				generationMatrix.setH(generationMatrix.getH() + 1);

	}

	private Matrix codeWord;
	private Matrix countedCodeWord;

	public void code() throws Exception {

		Matrix p1 = getP1();
		p1.show();
		Matrix p2 = getP2(p1);
		p2.show();
		codeWord = Matrix.CreateMatrix(codeWord, p1, p2);

		// codeWord = MatrixMath.multiply(codeWord, generationMatrix);
	}

	private Matrix getP2(Matrix p1) throws Exception {
		Matrix inverserTTransposed = MatrixMath.multiplyByConstant(MatrixMath.inverse(Matrix.getT(generationMatrix)), false);
		Matrix mulitplyAByCodeword = MatrixMath.multiply(MatrixMath.transpose(Matrix.getA(generationMatrix)), MatrixMath.transpose(codeWord));
		Matrix mulitplyBByP1 = MatrixMath.multiply(Matrix.getB(generationMatrix), MatrixMath.transpose(p1));
		return MatrixMath.multiply(inverserTTransposed, MatrixMath.MatrixBooleanAdd(mulitplyAByCodeword, mulitplyBByP1));
	}

	private Matrix getP1() throws Exception {
		Matrix mulitplyByconstant = MatrixMath.multiplyByConstant(Matrix.getE(generationMatrix), false);
		Matrix mulitplyByGeneration = MatrixMath.multiply(mulitplyByconstant, MatrixMath.inverse(Matrix.getT(generationMatrix)));
		Matrix mulitplyByA = MatrixMath.multiply(mulitplyByGeneration, Matrix.getA(generationMatrix));
		Matrix substractC = MatrixMath.MatrixBooleanSubstract(mulitplyByA, Matrix.getC(generationMatrix));
		Matrix multilyByCodeword = MatrixMath.multiply(getPhi(), substractC);
		Matrix p1 = MatrixMath.multiply(multilyByCodeword, MatrixMath.transpose(codeWord));
		return p1;
	}

	private Matrix getPhi() throws Exception {
		Matrix mulitplyByconstant = MatrixMath.multiplyByConstant(Matrix.getE(generationMatrix), false);
		Matrix mulitplyByGeneration = MatrixMath.multiply(mulitplyByconstant, MatrixMath.inverse(Matrix.getT(generationMatrix)));
		Matrix mulitplyByB = MatrixMath.multiply(mulitplyByGeneration, Matrix.getB(generationMatrix));
		Matrix addD = MatrixMath.MatrixBooleanAdd(mulitplyByB, Matrix.getD(generationMatrix));
		return addD;
	}

	public void decode(Matrix codeWord) {
		countedCodeWord = new Matrix(1, columns);
		Matrix countedNode = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			int bitNumber = 0;
			Matrix countedCheckWord = new Matrix(1, generationMatrix.getOnesCount(i));
			for (int l = 0; l < columns; l++)
				if (generationMatrix.get(i, l)) {
					countedCheckWord.set(0, bitNumber, codeWord.get(0, l));
					bitNumber++;
				}
			for (int k = 0; k < countedCheckWord.getCols(); k++) {
				boolean out = false;
				for (int l = 0; l < countedCheckWord.getCols(); l++) {
					if (l != k)
						out = MatrixMath.BooleanAdd(out, countedCheckWord.get(0, l));
				}
				countedNode.set(i, k, out);
			}

		}
		for (int i = 0; i < columns; i++) {
			int onesCount = 0;
			int zerosCount = 0;
			for (int k = 0; k < rows; k++) {
				if (generationMatrix.get(k, i)) {
					if (countedNode.get(k, generationMatrix.getOneNumber(k, i)))
						onesCount++;
					else
						zerosCount++;
				}
			}
			if (codeWord.get(0, i))
				onesCount++;
			else
				zerosCount++;

			countedCodeWord.set(0, i, onesCount > zerosCount);
		}

	}

	public Matrix getCodeWord() {
		return codeWord;
	}

	public void setCodeWord(Matrix toCode) {
		this.codeWord = toCode;
	}

	public Matrix getCountedCodeWord() {
		return countedCodeWord;
	}

	public void setCountedCodeWord(Matrix countedCodeWord) {
		this.countedCodeWord = countedCodeWord;
	}
}
