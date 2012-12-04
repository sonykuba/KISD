package com.pwr.kisd;

public class Console {

	public static void main(final String args[]) throws Exception {
		Matrix codeWord = new Matrix(Matrix.generateCodeWord(248));
		LDPC coder = new LDPC(8, 8 + codeWord.getCols());
		coder.setCodeWord(new Matrix(codeWord));

		coder.code();
		coder.getCodeWord().show();
		coder.getCodeWord().flip(0, 1);
		Matrix tempMatrix = coder.getCodeWord();
		int iterations = 0;
		while (true) {
			coder.decode(tempMatrix);
			coder.getCodeWord().show();
			coder.getCountedCodeWord().show();
			int errors = Matrix.compareMatrixs(tempMatrix, coder.getCountedCodeWord());
			System.out.println("iterations:" + iterations + " errors:" + errors);
			iterations++;
			if (errors == 0)
				break;
			tempMatrix = coder.getCountedCodeWord();

		}
		int errors = Matrix.compareMatrixs(codeWord, coder.getCountedCodeWord());
		System.out.println("Finished after :" + iterations + " iterations. Total errors:" + errors);

	}
}
