import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Worker implements Runnable {
	Tree section;
	CyclicBarrier synchPoint;
	Region[][] myPart;
	boolean running;
	Lock lock = new ReentrantLock();
	Condition notReady = lock.newCondition();
	boolean wasSignalled = false;

	public Worker(CyclicBarrier barrier){
		this.section = null;
		this.synchPoint = barrier;
		this.myPart = null;
		this.running = true;
	}

	public void run(){
		while (running){
			try{
				lock.lock();
				try {
					while (!wasSignalled){
						notReady.await();
					}
					wasSignalled = false;
				} finally {
					lock.unlock();
				}
				//section.compute(); // returns its parts of the alloy
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

	public void wakeUp(Tree section){
		lock.lock();
		try {
			wasSignalled = true;
			notReady.signal();
		} finally {
			lock.unlock();
			this.section = section;
			//this.section.setWorker(this);
		}
	}

	public void setSection(Tree section){
	
	}

	public void setPart(Region[][] part){
		myPart = part;
		//Merger.addPart(myPart);
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