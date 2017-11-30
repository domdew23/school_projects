import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Worker implements Runnable {
	Tree section;
	CyclicBarrier synchPoint;
	Region[][] myPart;
	boolean running;
	public Worker(CyclicBarrier barrier){
		this.section = null;
		this.synchPoint = barrier;
		this.myPart = null;
		this.running = true;
	}

	public void run(){
		//while (running){
			try{
				wait();
				section.compute(); // returns its parts of the alloy
				Merger.addPart(myPart);
				System.out.println(Thread.currentThread().getName() + " added my part");
				try {
					synchPoint.await();
				} catch (BrokenBarrierException e){
					e.printStackTrace();
				}
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		//}
	}

	public void wakeUp(){
		notify();
	}

	public void setSection(Tree section){
		this.section = section;
		this.section.setWorker(this);
	}

	public void setPart(Region[][] part){
		myPart = part;
	}

	public void printPart(){
		System.out.println(Thread.currentThread().getName() + ":\n");
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				System.out.println(myPart[i][j]);
			}
		}
	}
}