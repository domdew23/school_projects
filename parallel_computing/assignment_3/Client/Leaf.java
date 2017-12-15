public class Leaf extends Tree {
	private int loRow;
	private int loCol;
	private int hiRow;
	private int hiCol;
	private int steps;
	public Region[][] A;
	public Region[][] B;

	public Leaf(Region[][] A, Region[][] B, int loRow, int hiRow, int loCol, int hiCol){
		this.A = A;
		this.B = B;
		this.loRow = loRow;
		this.hiRow = hiRow;
		this.loCol = loCol;
		this.hiCol = hiCol;
		this.steps = 0;
	}

	public Region[][] compute(){
		Region[][] a = A;
		Region[][] b = B;
		double md = 0.0;

		for (int x = loCol; x < hiCol; x++){
			for (int y = loRow; y < hiRow; y++){
				b[y][x] = a[y][x].compute();
								
				double diff = b[y][x].getTemp() - a[y][x].getTemp();
				md = Math.max(md, Math.abs(diff));
			}
		}
		//System.out.println("md: " + md);
		//Control.addPart(b);
		//Control.updateNeighbors(b);
		maxDiff = md;
		return b;
	}

	public void update(Chunk chunk){
		this.A = chunk.elements;
		this.B = new Region[Settings.CHUNK_SIZE][Settings.WIDTH];
	}

	public String toString(){
		return "loRow: " + loRow + " | hiRow: " + hiRow + " | loCol: " + loCol + " | hiCol: " + hiCol;
	}
}