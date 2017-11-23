public class Interior extends Tree {
	private final Tree[] quads;
	public Tree q1,q2,q3,q4;
	
	Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
	}

	public void compute(){
		for (int i = 0; i < quads.length; i++){
			quads[i].compute();
		}
	}

	public String toString(){
		return "q1:\n" + q1 + "\n\nq2:\n" + q2 + "\n\nq3:\n" + q3 + "\n\nq4:\n" + q4;
	}
}