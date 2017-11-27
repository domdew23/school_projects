import java.util.concurrent.CyclicBarrier;

public class Interior extends Tree {
	private final Tree[] quads;
	public final Tree q1,q2,q3,q4;
	public CyclicBarrier synchPoint = new CyclicBarrier(4, new Runner());
	
	Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
	}

	public void compute(){
		for (int i = 0; i < quads.length; i++){
			// create threads here with their respective indices and compute
			// threads share instance of cyclicbarrier
			Worker worker = new Worker(quads[i], synchPoint);
			Thread t = new Thread(worker);
			t.start();
		}
		System.out.println("back in main thread");
	}

	public void reinitialize(){
		// get ready for next iteration of the computations
		Region[][] alloy;
		for (int i = 0; i < quads.length; i++){

		}
	}

	public String toString(){
		return "q1:\n" + q1 + "\n\nq2:\n" + q2 + "\n\nq3:\n" + q3 + "\n\nq4:\n" + q4;
	}
}