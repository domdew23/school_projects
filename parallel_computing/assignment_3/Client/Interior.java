import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.ArrayList;

public class Interior extends Tree {
	private final Tree[] quads;
	private Region[][] finalResult = new Region[Settings.CHUNK_SIZE][Settings.WIDTH];
	
	public Interior(Tree q1, Tree q2, Tree q3, Tree q4){
		quads = new Tree[]{q1, q2, q3, q4};
	}

	public Region[][] compute(){
		invokeAll(quads);
		double md = 0.0;
		for (int i = 0; i < quads.length; i++){
			md = Math.max(md, quads[i].maxDiff);
			Region[][] result = quads[i].getRawResult();
			
			combine(result);
			quads[i].reinitialize();
		}
		maxDiff = md;
		return finalResult;
	}

	public void update(Chunk chunk){
		for (int i = 0; i < quads.length; i++){
			quads[i].update(chunk);
		}
	}

	public void combine(Region[][] res){
		for (int y = 0; y < Settings.CHUNK_SIZE;y++){
			for (int x = 0; x < Settings.WIDTH; x++){
				if (res[y][x] != null){
					finalResult[y][x] = res[y][x];
				}
			}
		}
	}

	public String toString(){
		return "q1:\n" + quads[0] + "\n\nq2:\n" + quads[1] + "\n\nq3:\n" + quads[2] + "\n\nq4:\n" + quads[3];
	}
}