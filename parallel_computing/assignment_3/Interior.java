import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Interior extends Tree {
	private final Tree[] quads;
	
	public Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
	}

	public void compute(){
		invokeAll(quads);
		double md = 0.0;
		for (int i = 0; i < quads.length; i++){
			md = Math.max(md, quads[i].maxDiff);
			quads[i].reinitialize();
		}
		maxDiff = md;
	}

	public String toString(){
		return "q1:\n" + quads[0] + "\n\nq2:\n" + quads[1] + "\n\nq3:\n" + quads[2] + "\n\nq4:\n" + quads[3];
	}
}