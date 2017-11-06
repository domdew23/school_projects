public class Press implements AtomicModel {
	int p; // number of parts for the machine to process
	int s; // time remaining to process first of the parts

	public Press(){

	}

	public boolean lambda(){
		//always outputs exactly one processed part
	}

	public void deltaInternal(){
		// new state -> (p - 1, t) where t = 1

	}

	public void deltaExternal(){

	}

	public void deltaConfluent(){
		// executed when new part arrives and part is completed simultaneously
		// should eject the completed part
	}

	public int timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return Integer.MAX_VALUE;
		}
	}
}