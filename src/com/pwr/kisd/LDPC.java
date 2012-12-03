package com.pwr.kisd;

import java.math.BigInteger;

public class LDPC {

	Matrix generationMatrix;
	int columns;
	int rows;

	public LDPC(int M, int N) {
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
		// generationMatrix = new Matrix(matrix);
		this.columns = N;
		this.rows = M;
	}

	private Matrix codeWord;
	private Matrix checkWord;
	private Matrix countedCheckWord;
	private Matrix countedCodeWord;
	private Matrix countedNode;

	public void code() throws Exception {
		Matrix p1 = getP1();
		p1.show();
		Matrix p2 = getP2(p1);
		p2.show();
		codeWord = Matrix.CreateMatrix(codeWord, p1, p2);
		// MatrixMath.multiply(new Matrix(new boolean[][] { new boolean[] {
		// false, false, false, false } }), generationMatrix).show();
		// checkWord =
		// MatrixMath.transpose(MatrixMath.multiply(generationMatrix,
		// MatrixMath.transpose(codeWord)));
	}

	private Matrix getP2(Matrix p1) throws Exception {
		Matrix inverserTTransposed = MatrixMath.multiplyByConstant(MatrixMath.inverse(Matrix.getT(generationMatrix)), false);
		Matrix mulitplyAByCodeword = MatrixMath.multiply(Matrix.getA(generationMatrix), MatrixMath.transpose(codeWord));
		Matrix mulitplyAByP1 = MatrixMath.multiply(Matrix.getB(generationMatrix), MatrixMath.transpose(p1));
		return MatrixMath.multiply(inverserTTransposed, MatrixMath.MatrixBooleanAdd(mulitplyAByCodeword, mulitplyAByP1));
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

	public void decode() {
		countedCodeWord = new Matrix(1, columns);
		countedNode = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			int sum = 0;
			int bitNumber = 0;
			countedCheckWord = new Matrix(1, generationMatrix.getOnesCount(i));
			for (int l = 0; l < columns; l++)
				if (generationMatrix.get(i, l)) {
					countedCheckWord.set(0, bitNumber, codeWord.get(0, l));
					bitNumber++;
				}
			for (int k = 0; k < countedCheckWord.getCols(); k++) {
				sum = 0;
				for (int l = 0; l < countedCheckWord.getCols(); l++) {
					if (l != k)
						sum = (sum + (countedCheckWord.get(0, l) ? 1 : 0)) % 2;
				}
				countedNode.set(i, k, sum == 1);
			}
			// countedNode.show();

		}

		for (int l = 0; l < columns; l++) {
			int onesCount = 0;
			int zerosCount = 0;
			int bitNumber = 0;
			for (int k = 0; k < rows; k++) {
				if (generationMatrix.get(k, l)) {
					if (countedNode.get(k, bitNumber))
						onesCount++;
					else
						zerosCount++;
					bitNumber++;
				}
			}
			if (codeWord.get(0, l))
				onesCount++;
			else
				zerosCount++;

			countedCodeWord.set(0, l, onesCount > zerosCount);
		}

	}

	public Matrix getCheckWord() {
		return checkWord;
	}

	public void setCheckWord(Matrix checkWord) {
		this.checkWord = checkWord;
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
