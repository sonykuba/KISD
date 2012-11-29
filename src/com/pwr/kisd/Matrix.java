package com.pwr.kisd;

public class Matrix
{
    private final int rows; // number of rows
    private final int columns; // number of columns
    private final boolean[][] data; // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N)
    {
	this.rows = M;
	this.columns = N;
	data = new boolean[M][N];
    }

    public Matrix(boolean[][] inverseMatrix)
    {
	this.rows = inverseMatrix.length;
	this.columns = inverseMatrix[0].length;
	data = inverseMatrix;
    }

    public Matrix(boolean[] codeWord)
    {
	this.rows = 1;
	this.columns = codeWord.length;
	data = new boolean[rows][columns];
	for (int i = 0; i < columns; i++)
	    data[0][i] = codeWord[i];
    }

    public int getRows()
    {
	return rows;
    }

    public Matrix getCol(int col)
    {
	Matrix result = new Matrix(columns, 1);
	for (int i = 0; i < columns; i++)
	    result.data[i][0] = data[i][col];
	return result;
    }

    public boolean isTheSame(Matrix matrix)
    {
	for (int i = 0; i < columns; i++)
	    for (int l = 0; l < rows; l++)
	    {
		if (get(l, i) != matrix.get(l, i))
		    return false;
	    }
	return true;
    }

    public int getCols()
    {
	return columns;
    }

    public void set(final int row, final int col, final boolean value)
    {
	data[row][col] = value;
    }

    public void flip(final int row, final int col)
    {
	data[row][col] = !data[row][col];
    }

    public boolean get(final int row, final int col)
    {
	return data[row][col];
    }

    public boolean[] toPackedArray()
    {
	boolean[] result = new boolean[rows * columns];
	for (int i = 0; i < columns; i++)
	    for (int l = 0; l < rows; l++)
	    {
		result[l * columns + i] = data[l][i];
	    }
	return result;
    }

    public void show()
    {
	System.out.println("");
	for (int i = 0; i < rows; i++)
	{
	    String temp = "";
	    for (int l = 0; l < columns; l++)
		temp += data[i][l] + " ";
	    System.out.println(temp);
	}
    }

    public String toString()
    {
	String temp = "";
	for (int i = 0; i < rows; i++)
	{
	    for (int l = 0; l < columns; l++)
		temp += data[i][l] + " ";
	    temp += " \n ";
	}
	return temp;
    }

    public void randomize()
    {
	for (int i = 0; i < rows; i++)
	    for (int l = 0; l < columns; l++)
	    {
		data[i][l] = Math.random() > 0.6;
	    }
    }

    public int getOnesCount(int row)
    {
	int sum = 0;
	for (int i = 0; i < columns; i++)
	    if (data[row][i])
		sum++;
	return sum;
    }

    public void sort()
    {
	int zerosAtPosition = 1;
	for(int k=0;k<columns;k++)
	{
	    int tempColumnNumber = -1;
	    for (int i = 0; i < columns; i++)
	    {
		int zerosCount = 0;
		for (int l = 0; l < zerosAtPosition ; l++)
		{
		    if (data[l][i])
		    {
			if (zerosAtPosition == zerosCount)
			{
			    tempColumnNumber = i;
			}
			break;
		    }
		    else
		    {
			zerosCount++;
		    }
		}
		if (tempColumnNumber == i)
		{
		    boolean[][] tempColumn = new boolean[data.length][1];
		    for (int j = 0; j < data.length; j++)
		    {
			tempColumn[j][0] = data[j][i];
		    }
		    for (int j = tempColumnNumber; j < columns-1; j++)
			for (int l = 0; l < rows; l++)
			{
			    data[l][j]=data[l][j+1];
			}
		    for (int j = 0; j < data.length; j++)
		    {
			data[j][data[0].length-1] = tempColumn[j][0];
		    }
		    zerosAtPosition++;
		}
	    }
	}
    }
}
