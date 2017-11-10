public class Machine implements AtomicModel {
	private int p; // number of parts for the machine to process
	private int s; // time remaining to process first of the parts
	private int t; // time it takes machine to finish a part
	private String name;
	private static Scheduler scheduler;
	private static Time currentTime;

	public Machine(int t, String name){
		this.t = t;
		this.name = name;
		this.p = 0;
		this.s = 0;
	}

	public int lambda(){
		//always outputs exactly one processed part
		return 1;
	}

	public void deltaInternal(){
		// new state -> (p - 1, t) where t = 1
		// schedule events from deltas
		p--;
		s = t;
		scheduleEvent("deltaInternal"); // sched event for coupled machines here, pass where its output goes as the new model
	}

	public void deltaExternal(int e, int q){
		// e = elapsed time | q = # of parts
		if (p > 0){
			p += q;
			s -= e;
		} else {
			p += q;
			s = t;
		}
		
		scheduleEvent("deltaExternal"); // maybe schedule deltaInternal here?
	}

	public void deltaConfluent(int q){
		// executed when new part arrives and part is completed simultaneously
		// should eject the completed part
		p += (q - 1);
		s = t;

		scheduleEvent("deltaConfluent");
	}

	public int timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return Integer.MAX_VALUE;
		}
	}

	// main loop advances time
	public void scheduleEvent(String kind){
		Time t = new Time(currentTime.getReal() + timeAdvance(), 0);
		Event e = new Event(t, kind, name);
		scheduler.put(e);
	}

	public static void setTime(Time t){
		currentTime = t;
	}

	public static Time getTime(){
		return currentTime;
	}

	public static void setScheduler(Scheduler s){
		scheduler = s;
	}

	public static Scheduler getScheduler(){
		return scheduler;
	}

	public String name(){
		return name;
	}

	public String toString(){
		return name + ": | p: " + p + " | s: " + s + " | t: " + t;
	}
}
