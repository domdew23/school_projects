public class Leaf extends Tree {
	private int loRow;
	private int loCol;
	private int hiRow;
	private int hiCol;
	private int steps;
	private Region[][] A;
	private Region[][] B;
	// A - old version
	// B - updated version

	Leaf(Region[][] A, Region[][] B, int loRow, int hiRow, int loCol, int hiCol){
		this.A = A;
		this.B = B;
		this.loRow = loRow;
		this.hiRow = hiRow;
		this.loCol = loCol;
		this.hiCol = hiCol;
		this.steps = 0;
	}

	public void compute(){
		boolean AtoB = (steps++ % 2) == 0;
		Region[][] a = (AtoB) ? A : B;
		Region[][] b = (AtoB) ? B : A;
		
		for (int i = loRow; i < hiRow; i++){
			for (int j = loCol; j < hiCol; j++){
				//System.out.println("col: " + j + " | row: " + i);
				// convergence -> converge all the new versions of the Alloy
				//System.out.println("Before: " + a[j][i]);
				b[j][i] = a[j][i].compute();
				//System.out.println("After: " + b[j][i]);
			}
		}
		System.out.println();
	}

	public String toString(){
		return "loRow: " + loRow + " | hiRow: " + hiRow + " | loCol: " + loCol + " | hiCol: " + hiCol;
	}
}