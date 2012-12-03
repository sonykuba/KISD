package com.pwr.kisd;

public class Console {

	public static void main(final String args[]) throws Exception {
		final boolean[] codeWord = Matrix.generateCodeWord(6);
		LDPC coder = new LDPC(6, 6 + codeWord.length);
		coder.setCodeWord(new Matrix(codeWord));
		coder.code();
		coder.getCodeWord().show();

		// coder.getCodeWord().flip(0, 3);

		coder.decode();
		coder.getCountedCodeWord().show();

		int errors = Matrix.compareMatrixs(coder.getCodeWord(), coder.getCountedCodeWord());
		System.out.println("errors:" + errors);
	}
}
