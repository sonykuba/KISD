package com.pwr.kisd;

public class MatrixMath
{

    public static Matrix transpose(Matrix input)
    {
	final boolean inverseMatrix[][] = new boolean[input.getCols()][input
		.getRows()];
	for (int r = 0; r < input.getRows(); r++)
	{
	    for (int c = 0; c < input.getCols(); c++)
	    {
		inverseMatrix[c][r] = input.get(r, c);
	    }
	}
	return new Matrix(inverseMatrix);

    }

    public static Matrix identity(int size)
    {
	final Matrix result = new Matrix(size, size);
	for (int i = 0; i < size; i++)
	{
	    result.set(i, i, true);
	}
	return result;
    }

    public static Matrix multiply(Matrix A, Matrix B)
    {
	if (A.getCols() != B.getRows())
	    throw new RuntimeException("Illegal matrix dimensions.");
	Matrix C = new Matrix(A.getRows(), B.getCols());
	for (int i = 0; i < C.getRows(); i++)
	    for (int j = 0; j < C.getCols(); j++)
		for (int k = 0; k < A.getCols(); k++)
		    if (B.get(k, j) && !A.get(i, k) || !B.get(k, j)
			    && A.get(i, k))
		    {//true
			if (C.get(i, j))
			    C.set(i, j, false);
			else
			    C.set(i, j, true);
		    }
		    else
		    {//false
			if (C.get(i, j))
			    C.set(i, j, true);
			else
			    C.set(i, j, false);
		    }

	return C;
    }

    public static Matrix multiplyMatrixCells(Matrix a, Matrix b)
    {
	final boolean result[][] = new boolean[a.getRows()][a.getCols()];
	for (int row = 0; row < a.getRows(); row++)
	{
	    for (int col = 0; col < a.getCols(); col++)
	    {
		result[row][col] = a.get(row, col) || b.get(row, col);
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
    public static String formatBoolean(final boolean b[])
    {
	final StringBuilder result = new StringBuilder();
	result.append('[');
	for (int i = 0; i < b.length; i++)
	{
	    if (b[i])
	    {
		result.append("T");
	    }
	    else
	    {
		result.append("F");
	    }
	    if (i != b.length - 1)
	    {
		result.append(",");
	    }
	}
	result.append(']');
	return (result.toString());
    }
}
