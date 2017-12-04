import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Interior extends Tree {
	private final Tree[] quads;
	public final Tree q1,q2,q3,q4;
	private final Worker[] workers;
	private final Thread[] threads;
	public CyclicBarrier synchPoint;
	
	Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
		Merger merger = new Merger();
		this.synchPoint = new CyclicBarrier(5, merger);
		this.workers = new Worker[quads.length];
		this.threads = new Thread[quads.length];
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
		invoke();
	}

	private void invoke(){
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
		wakeUp();
		waitUntilComplete();
		synchPoint.reset();
		double md = 0.0;
		for (int i = 0; i < quads.length; i++){
			md = Math.max(md, quads[i].maxDiff);
			quads[i].reset();
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

	public void reinitialize(){
		Region[][] alloy;
		for (int i = 0; i < quads.length; i++){
		}
	}

	public String toString(){
		return "q1:\n" + q1 + "\n\nq2:\n" + q2 + "\n\nq3:\n" + q3 + "\n\nq4:\n" + q4;
	}
}