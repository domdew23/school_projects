public class Jacobi {
	Tree root;
	int maxSteps;

	Jacobi(Region[][] A, Region[][] B, int firstRow, int lastRow, int firstCol, int lastCol, int maxSteps, int cellsPerLeaf){
		this.maxSteps = maxSteps;
		root = build(A, B, firstRow, lastRow, firstCol, lastCol, cellsPerLeaf);
	}

	public void compute(){
		for (int i = 0; i < maxSteps; i++){
			root.compute();
		}
	}

	Tree build(Region[][] A, Region[][] B, int loRow, int hiRow, int loCol, int hiCol, int size){
   		if ((hiRow - loRow) * (hiCol - loCol) <= size){
   			return new Leaf(A, B, loRow, hiRow, loCol, hiCol);
   		}
   		int midRow = (loRow + hiRow) / 2;
   		int midCol = (loCol + hiCol) / 2;
   		return new Interior(build(A, B, loRow, midRow, loCol, midCol, size),
   							build(A, B, loRow, midRow, midCol, hiCol, size),
   							build(A, B, midRow, hiRow, loCol, midCol, size),
   							build(A, B, midRow, hiRow, midCol, hiCol, size));
	}
}