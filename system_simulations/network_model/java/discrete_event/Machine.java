import java.util.ArrayList;

public class Machine<Input, Output> implements AtomicModel<Input, Output> {
	private int p; // number of parts for the machine to process
	private int s; // time remaining to process first of the parts
	private int t; // time it takes machine to finish a part
	private String name;
	private static Scheduler scheduler;
	private static Time currentTime;
	private ArrayList<Input> inputs;
	private ArrayList<Output> outputs;

	public Machine(int t, String name){
		this.name = name;
		this.t = t;
		this.p = 0;
		this.s = 0;
		this.inputs = new ArrayList<Input>();
		this.outputs = new ArrayList<Output>();
	}

	public int lambda(){
		//always outputs exactly one processed part
		return 1;
	}

	public void deltaInternal(int q){
		// new state -> (p - 1, t) where t = 1
		// schedule events from deltas
		p--;
		s = t;

		scheduleEvent(new Time(currentTime.getReal(), 0), "lambda", this, -1, -1);
		
		for (Output o : outputs){
			scheduleEvent(new Time(currentTime.getReal(), 0), "deltaExternal", o, -1, q);
		}
	}

	public void deltaExternal(double e, int q){
		// e = elapsed time | q = # of parts
		if (p > 0){
			p += q;
			s -= e;
		} else {
			p += q;
			s = t;
		}
		scheduleEvent(new Time(currentTime.getReal() + timeAdvance(), 0), "deltaInternal", this, -1, -1);
	}

	public void deltaConfluent(int q, int output){
		// executed when new part arrives and part is completed simultaneously
		// should eject the completed part
		p += (q - 1);
		s = t;

		scheduleEvent(new Time(currentTime.getReal() + timeAdvance(), 0), "deltaInternal", this, -1, q);
		
		for (Output o : outputs){
			scheduleEvent(new Time(currentTime.getReal(), 0), "deltaExternal", o, -1, output);
		}
	}

	public int timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return Integer.MAX_VALUE;
		}
	}

	// main loop advances time
	public Event scheduleEvent(Time t, String k, AtomicModel m, int e, int q){
		Event event;
		switch (e){
			case -1:
				if (q == -1){
					event = Event.builder(t, k, m).build();
					break;
				}
				event = Event.builder(t, k, m).addParameter(q).build();
				break;
			default:
				if (q == -1){
					event = Event.builder(t, k, m).addParameter(e).build();
					break;
				}
				event = Event.builder(t, k, m).addParameter(e).addParameter(q).build();
				break;
		}
		scheduler.put(event);
	}

	public void addInput(Input I){
		inputs.add(I);
	}

	public void addOutput(Output O){
		outputs.add(O);
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
