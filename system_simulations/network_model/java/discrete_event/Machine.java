public class Machine implements AtomicModel {
	private int p; // number of parts for the machine to process
	private int s; // time remaining to process first of the parts
	private int t; // time it takes machine to finish a part

	public Machine(int t){
		this.t = t;
	}

	public int lambda(){
		//always outputs exactly one processed part
		return 1;
	}

	public void deltaInternal(){
		// new state -> (p - 1, t) where t = 1
		// schedule events from deltas
		p-=1;
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

	public String toString(){
		return "Press";
	}
}
