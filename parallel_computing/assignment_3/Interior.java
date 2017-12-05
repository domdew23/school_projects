import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Interior extends Tree {
	private final Tree[] quads;
	private final Worker[] workers;
	private final Thread[] threads;
	public CyclicBarrier synchPoint;
	
	public Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
		Merger merger = new Merger();
		this.synchPoint = new CyclicBarrier(5, merger);
		this.workers = new Worker[quads.length];
		this.threads = new Thread[quads.length];
		init();
	}

	private void init(){
		for (int i = 0; i < quads.length; i++){
			workers[i] = new Worker(synchPoint);
			threads[i] = new Thread(workers[i]);
			threads[i].start();
		}
	}

	public void wakeUp(){
		for (int i = 0; i < quads.length; i++){
			workers[i].wakeUp(quads[i]);
		}
	}

	public void compute(){
		// invoke quads
		invokeAll(quads);
		//wakeUp();
		//waitUntilComplete();
		//synchPoint.reset();
		double md = 0.0;
		for (int i = 0; i < quads.length; i++){
			md = Math.max(md, quads[i].maxDiff);
			quads[i].reinitialize();
		}
		maxDiff = md;
	}

	private void waitUntilComplete(){
		try {
			try {
				synchPoint.await();
			} catch (BrokenBarrierException e){
				e.printStackTrace();
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	public String toString(){
		return "q1:\n" + quads[0] + "\n\nq2:\n" + quads[1] + "\n\nq3:\n" + quads[2] + "\n\nq4:\n" + quads[3];
	}
}