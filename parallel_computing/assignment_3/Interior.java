import java.util.concurrent.CyclicBarrier;

public class Interior extends Tree {
	private final Tree[] quads;
	public final Tree q1,q2,q3,q4;
	private final Worker[] workers;
	public CyclicBarrier synchPoint;
	
	Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
		this.synchPoint = new CyclicBarrier(quads.length, new Merger());
		this.workers = new Worker[quads.length];
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
		invoke();
	}

	private void invoke(){
		for (int i = 0; i < quads.length; i++){
			workers[i] = new Worker(synchPoint);
		}
	}

	public void compute(){
		for (int i = 0; i < quads.length; i++){
			// resuse threads don't make new ones (too much overhead)
			Thread thread = new Thread(workers[i]);
			workers[i].setSection(quads[i]);
			thread.start();
		}
		try {Thread.sleep(1000);} catch (InterruptedException e){}
	}

	public void reinitialize(){
		// get ready for next iteration of the computations
		// get same threads ready
		Region[][] alloy;
		for (int i = 0; i < quads.length; i++){

		}
	}

	public String toString(){
		return "q1:\n" + q1 + "\n\nq2:\n" + q2 + "\n\nq3:\n" + q3 + "\n\nq4:\n" + q4;
	}
}