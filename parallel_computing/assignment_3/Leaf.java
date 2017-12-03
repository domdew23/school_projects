public class Leaf extends Tree {
	private int loRow;
	private int loCol;
	private int hiRow;
	private int hiCol;
	private int steps;
	private Worker myWorker;
	public Region[][] A;
	public Region[][] B;

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
		//System.out.println(Thread.currentThread().getName() + " step: " + steps);
		boolean AtoB = (steps++ % 2) == 0;
		Region[][] a = (AtoB) ? A : B;
		Region[][] b = (AtoB) ? B : A;
		double md = 0.0;

		for (int i = loCol; i < hiCol; i++){
			for (int j = loRow; j < hiRow; j++){
				b[i][j] = a[i][j].compute();
				md = Math.max(md, Math.abs(b[i][j].getTemp() - a[i][j].getTemp()));
			}
		}
		myWorker.setPart(b);
		maxDiff = md;
		Control.updateNeighbors(b);
	}

	public void reset(){
		//maxDiff = 0.0;
	}

	public void setWorker(Worker w){
		this.myWorker = w;
	}

	public String toString(){
		return "loRow: " + loRow + " | hiRow: " + hiRow + " | loCol: " + loCol + " | hiCol: " + hiCol;
	}
}