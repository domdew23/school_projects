/* run this when everyone is done, should put all the pieces together */
import java.util.ArrayList;

public class Merger implements Runnable {
	private volatile static ArrayList<Region[][]> quads;
	private static Region[][] updatedAlloy;

	public Merger(){
		updatedAlloy = new Region[Settings.WIDTH][Settings.HEIGHT];
	 	quads = new ArrayList<Region[][]>();
	}

	public void run(){
		for (int k = 0; k < quads.size(); k++){
			Region[][] r = quads.get(k);
			for (int i = 0; i < Settings.WIDTH; i++){
				for (int j = 0; j < Settings.HEIGHT; j++){
					updatedAlloy[i][j] = r[i][j];
					//updatedAlloy[i][j].calcRGB();
				}
			}
		}
		// maybe here have threads start computations again (reset the barrier)
	}

	public synchronized static void addPart(Region[][] part){
		quads.add(part);
	}

	public synchronized static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}