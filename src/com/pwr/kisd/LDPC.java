package com.pwr.kisd;

import java.math.BigInteger;

public class LDPC
{

    Matrix generationMatrix;
    int columns;
    int rows;

    public LDPC(int M, int N)
    {
	generationMatrix = new Matrix(M, N);
	generationMatrix.randomize();
	generationMatrix.sort();
	generationMatrix.show();
	/*boolean[][] matrix = new boolean[][]
	{ new boolean[]
	{ false, true, false, true, true, false, false, true }, new boolean[]
	{ true, true, true, false, false, true, false, false }, new boolean[]
	{ false, false, true, false, false, true, true, true }, new boolean[]
	{ true, false, false, true, true, false, true, false } };*/
	//generationMatrix = new Matrix(matrix);
	this.columns = N;
	this.rows = M;
    }

    private Matrix codeWord;
    private Matrix checkWord;
    private Matrix countedCheckWord;
    private Matrix countedCodeWord;
    private Matrix countedNode;

    public void code()
    {
	MatrixMath.multiply(new Matrix(new boolean[][]{new boolean[]{false,false,false,false}}),
		generationMatrix).show();
	checkWord = MatrixMath.transpose(MatrixMath.multiply(generationMatrix,
		MatrixMath.transpose(codeWord)));
    }

    public void decode()
    {
	countedCodeWord = new Matrix(1, columns);
	countedNode = new Matrix(rows, columns);
	for (int i = 0; i < rows; i++)
	{
	    int sum = 0;
	    int bitNumber = 0;
	    countedCheckWord = new Matrix(1, generationMatrix.getOnesCount(i));
	    for (int l = 0; l < columns; l++)
		if (generationMatrix.get(i, l))
		{
		    countedCheckWord.set(0, bitNumber, codeWord.get(0, l));
		    bitNumber++;
		}
	    for (int k = 0; k < countedCheckWord.getCols(); k++)
	    {
		sum = 0;
		for (int l = 0; l < countedCheckWord.getCols(); l++)
		{
		    if (l != k)
			sum = (sum + (countedCheckWord.get(0, l) ? 1 : 0)) % 2;
		}
		countedNode.set(i, k, sum == 1);
	    }
	    // countedNode.show();

	}

	for (int l = 0; l < columns; l++)
	{
	    int onesCount = 0;
	    int zerosCount = 0;
	    int bitNumber = 0;
	    for (int k = 0; k < rows; k++)
	    {
		if (generationMatrix.get(k, l))
		{
		    if (countedNode.get(k, bitNumber))
			onesCount++;
		    else
			zerosCount++;
		    bitNumber++;
		}
	    }
	    if (codeWord.get(0,l ))
		onesCount++;
	    else
		zerosCount++;

	    countedCodeWord.set(0, l, onesCount > zerosCount);
	}

    }

    public Matrix getCheckWord()
    {
	return checkWord;
    }

    public void setCheckWord(Matrix checkWord)
    {
	this.checkWord = checkWord;
    }

    public Matrix getCodeWord()
    {
	return codeWord;
    }

    public void setCodeWord(Matrix toCode)
    {
	this.codeWord = toCode;
    }

    public Matrix getCountedCodeWord()
    {
	return countedCodeWord;
    }

    public void setCountedCodeWord(Matrix countedCodeWord)
    {
	this.countedCodeWord = countedCodeWord;
    }
}
