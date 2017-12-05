/* run this when everyone is done, should put all the pieces together */
import java.util.ArrayList;

public class Merger implements Runnable {
	private volatile static Region[][] updatedAlloy;

	public Merger(){
		updatedAlloy = new Region[Settings.WIDTH][Settings.HEIGHT];
	}

	public void run(){
		Control.updateNeighbors(updatedAlloy);
	}
}