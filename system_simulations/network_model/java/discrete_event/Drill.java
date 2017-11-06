public class Drill implements AtomicModel {
	int p;
	int s;

	public Drill(){

	}

	public boolean lambda(){

	}

	public void deltaInternal(){
		// new state -> (p - 1, t) where t = 2

	}

	public void deltaExternal(){

	}

	public void deltaConfluent(){

	}

	public int timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return Integer.MAX_VALUE;
		}
	}
}