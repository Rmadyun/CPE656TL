package TestNavigation;

import java.util.Arrays;

///Class represents a matrix of floating point numbers
public class Matrix {
	
	private int numberOfRows;
	private int numberOfColumns;
	private double matrix[][];
	
	///Constructor
	public Matrix(int numberOfRows, int numberOfColumns){
		
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		
		matrix = new double[numberOfRows][numberOfColumns];
		
		//Initialize matrix
        zero(this);
	}
	
	///Multiplies this matrix against another.
	///Returns the product of the matrix multiplication. Returns null
	///if multiplication is not possible.
	public Matrix multiply(Matrix otherMatrix){
		return multiply(this, otherMatrix);
	}

	///Multiplies a matrix against another.
	///Returns the product of the matrix multiplication. Returns null
	///if multiplication is not possible.
	public static Matrix multiply(Matrix firstMatrix, Matrix secondMatrix){
		Matrix productMatrix = null;
		
		if(firstMatrix.numberOfColumns == secondMatrix.numberOfRows)
		{
			productMatrix = new Matrix(firstMatrix.numberOfRows, secondMatrix.numberOfColumns);
			for(int i = 0; i < firstMatrix.numberOfRows; i++)
			{
				for(int j= 0; j < secondMatrix.numberOfColumns; j++)
				{
					for(int k=0; k <firstMatrix.numberOfColumns; k++){
						
						productMatrix.matrix[i][j] += firstMatrix.matrix[i][k] * secondMatrix.matrix[k][j];	
					}
				}
			}
		}
		
		return productMatrix;
	}
	
	///Multiplies a matrix against with a scalar value
	///Returns the product of the matrix multiplication. Returns null
	///if multiplication is not possible.
	public Matrix multiply(double scalar){
			return multiply(this, scalar);
	}
	
	///Multiplies a matrix against with a scalar value
	///Returns the product of the matrix multiplication. Returns null
	///if multiplication is not possible.
	public static Matrix multiply(Matrix matrix, double scalar){
		Matrix productMatrix = null;
		
		if(matrix != null)
		{
			productMatrix = new Matrix(matrix.numberOfRows, matrix.numberOfColumns);
			for(int i = 0; i < matrix.numberOfRows; i++)
			{
				for(int j=0; j <matrix.numberOfColumns; j++){
						
					productMatrix.matrix[i][j] += matrix.matrix[i][j] * scalar;	
				}		
			}
		}
		
		return productMatrix;
	}
	
	///Adds this matrix against another.
	///Returns the sum of the matrix addition. Returns null
	///if sum is not possible.
	public Matrix add(Matrix otherMatrix){
		return add(this, otherMatrix);
	}
	
	///Adds a matrix against another.
	///Returns the sum of the matrix addition. Returns null
	///if sum is not possible.
	public static Matrix add(Matrix firstMatrix, Matrix secondMatrix){
		Matrix sumMatrix = null;
		
		if((firstMatrix.numberOfColumns == secondMatrix.numberOfColumns)
				&&(firstMatrix.numberOfRows == secondMatrix.numberOfRows)){
			
			sumMatrix = new Matrix(firstMatrix.numberOfRows, firstMatrix.numberOfColumns);
			
			for(int i = 0; i < firstMatrix.numberOfRows; i++)
			{
				for(int j = 0; j < firstMatrix.numberOfColumns; j++){
					sumMatrix.matrix[i][j] = firstMatrix.matrix[i][j] + secondMatrix.matrix[i][j];
				}
			}
		}
		
		return sumMatrix;
	}
	
	///Fills a matrix with zeroes
	public static void zero(Matrix matrix)
	{
		for(int i = 0; i < matrix.numberOfRows; i++)
		{
			for(int j = 0; j < matrix.numberOfColumns; j++){
				matrix.matrix[i][j] = 0;
			}
		}
	}
	
	public Matrix transpose(){
		
		return transpose(this);
	}
	
	public static Matrix transpose(Matrix matrix){
		Matrix transposeMatrix = new Matrix(matrix.numberOfColumns, matrix.numberOfRows);
		
		for(int i = 0; i < matrix.numberOfRows; i++)
		{
			for(int j = 0; j < matrix.numberOfColumns; j++){
				transposeMatrix.matrix[j][i] = matrix.matrix[i][j];
			}
		}
		
		return transposeMatrix;
	}
	
	public static Matrix identity(int size) {
		Matrix identityMatrix = new Matrix(size, size);
		
		for(int i = 0; i < size; i++){
			identityMatrix.matrix[i][i] = 1;
		}
		
		return identityMatrix;
	}

	///Returns the number of columns in the matrix
	public int getNumberOfColumns(){
		return numberOfColumns;
	}
	
	///Returns the number of rows in the matrix
	public int getNumberOfRows(){
		
		return numberOfRows;
	}
	
	//Reads the current value of a cell in the matrix
	public double getValue(int row, int column){
		double cellValue = Double.NaN;
		
		if((row>=0&& row<numberOfRows)&&(column >=0 && column < numberOfColumns)){
			cellValue = matrix[row][column];
		}
		
		return cellValue;
	}
	
	///Assigns a value to a cell in the matrix
	public void setValue(int row, int column, double value)
	{
		if((row>=0&& row<numberOfRows)&&(column >=0 && column < numberOfColumns)){
			matrix[row][column] = value;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(matrix);
		result = prime * result + numberOfColumns;
		result = prime * result + numberOfRows;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(matrix, other.matrix))
			return false;
		if (numberOfColumns != other.numberOfColumns)
			return false;
		if (numberOfRows != other.numberOfRows)
			return false;
		return true;
	}

}
