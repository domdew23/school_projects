import java.util.concurrent.RecursiveAction;

public class Jacobi extends RecursiveAction implements Runnable{
	Tree root;
	int maxSteps;
	Thread thread;
	double EPSILON = 0.001;
	
	Jacobi(Region[][] A, Region[][] B, int firstRow, int lastRow, int firstCol, int lastCol, int maxSteps, int cellsPerLeaf){
		this.maxSteps = maxSteps;
		root = build(A, B, firstRow, lastRow, firstCol, lastCol, cellsPerLeaf);
		System.out.println(root);
	}

	public void compute(){
		long lastTime = System.currentTimeMillis();
		double delta = 0;
		long now = 0;
		for (int i = 0; i < maxSteps; i++){
			now = System.currentTimeMillis();
			root.invoke();
			lastTime = System.currentTimeMillis();
			delta = (lastTime - now);
			//System.out.println("Step took: {" + delta + "} milliseconds.");
			System.out.println(root.maxDiff);
			if (root.maxDiff < EPSILON){
				Settings.RUNNING = false;
				System.out.println("Converged.");
				return;			
			} else {
				root.reinitialize();
			}
		}
	}

	public void run(){
		for (int i = 0; i < maxSteps; i++){
			//root.compute();
			if (root.maxDiff < EPSILON){
				Settings.RUNNING = false;
				System.out.println("Converged.");
				break;
			}
		}
		stop();
	}

	public synchronized void start(){
		thread = new Thread(this, "Jacobi");
		thread.start();
	}

	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	private Tree build(Region[][] A, Region[][] B, int loRow, int hiRow, int loCol, int hiCol, int size){
   		if ((hiRow - loRow) * (hiCol - loCol) <= size){
   			return new Leaf(A, B, loRow, hiRow, loCol, hiCol);
   		}

   		int midRow = (loRow + hiRow) / 2;
   		int midCol = (loCol + hiCol) / 2;
   		return new Interior(build(A, B, loRow, midRow, loCol, midCol, size),
   							build(A, B, loRow, midRow, midCol, hiCol, size),
   							build(A, B, midRow, hiRow, loCol, midCol, size),
   							build(A, B, midRow, hiRow, midCol, hiCol, size));
	}
}