/* run this when everyone is done, should put all the pieces together */
import java.util.ArrayList;

public class Merger implements Runnable {
	private volatile static Region[][] updatedAlloy;

	public Merger(){
		updatedAlloy = new Region[Settings.WIDTH][Settings.HEIGHT];
	}

	public void run(){
		Control.updateNeighbors(updatedAlloy);
		Control.prevVersion = updatedAlloy;
	}

	public static void addPart(Region[][] part){
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				if (part[i][j] != null){
					updatedAlloy[i][j] = part[i][j];
				}
			}
		}
	}

	public synchronized static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}