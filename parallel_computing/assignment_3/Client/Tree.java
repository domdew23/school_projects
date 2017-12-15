import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
abstract class Tree extends RecursiveTask<Region[][]> {
	volatile double maxDiff;
	public void update(Chunk chunk){}
}
