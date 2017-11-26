import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Worker implements Runnable {
	Tree section;
	CyclicBarrier synchPoint;
	public Worker(Tree section, CyclicBarrier barrier){
		this.section = section;
		this.synchPoint = barrier;
	}

	public void run(){
		try{
			System.out.println(Thread.currentThread().getName() + " running " + section);
			section.compute();
			try {
				synchPoint.await();
			} catch (BrokenBarrierException e){
				e.printStackTrace();
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}