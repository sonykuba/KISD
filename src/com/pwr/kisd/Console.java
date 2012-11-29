package com.pwr.kisd;

public class Console {

	public static void main(final String args[]) {
		final boolean[] codeWord = { true, false, false, true, false, true, false, true };
		LDPC coder = new LDPC(4, 4+codeWord.length);
		coder.setCodeWord(new Matrix(codeWord));
		coder.code();
		new Matrix(codeWord).show();

		coder.getCodeWord().flip(0, 3);
		
		coder.decode();
		coder.getCountedCodeWord().show();
	}
}
