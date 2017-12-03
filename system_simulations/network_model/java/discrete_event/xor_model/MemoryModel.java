/* Leaf */
import java.util.ArrayList;

public class MemoryModel<I,O> implements AtomicModel<I,O>{
	private boolean[] state = new boolean[2];
	private ArrayList<I> inputs;
	private ArrayList<O> outputs;
	private ArrayList<Boolean> bits;
	public static Time currentTime;
	public static Scheduler<AtomicModel> scheduler;

	public MemoryModel(){
		this.state[0] = false;
		this.state[1] = false;
		inputs = new ArrayList<I>();
		outputs = new ArrayList<O>();
		bits = new ArrayList<Boolean>();
	}

	public boolean lambda(){
		//System.out.println("M Output: " + ((state[0] ^ state[1]) ? 1 : 0));
		//System.out.println("==================");
		return state[0];
	}

	public void delta(boolean[] X){
		System.out.println("M Input: " + ((X[0]) ? 1 : 0) + ((X[1]) ? 1 : 0));
		state[0] = state[1];
		state[1] = X[1];
		System.out.println("M New State: " + ((state[0]) ? 1 : 0) + " || " + ((state[1]) ? 1 : 0) + "\n==================");
	}

	public void deltaExternal(boolean x){
		bits.add(x);
		double t = currentTime.getReal() + timeAdvance();
		Event<AtomicModel> event = Event.builder(new Time(t, 0), "deltaInternal", this).build();
		scheduler.put(event);
	}

	public void deltaInternal(){
		state[0] = state[1];
		state[1] = bits.get(0);
		for (O out : outputs){
			Event<AtomicModel> event = Event.builder(currentTime, "deltaExternal", out).addParameter(lambda()).build();
			scheduler.put(event);
		}
	}

	public void deltaConfluent(boolean x){
		deltaExternal(x);
		deltaInternal();
	}

	public void addInput(I in){
		inputs.add(in);
	}

	public void addOutput(O out){
		outputs.add(out);
	}

	public ArrayList<I> getInputs(){
		return inputs;
	}

	public int timeAdvance(){
		return 1;
	}

	public String toString(){
		return "MemoryModel | State: " + ((state[0]) ? 1 : 0) + " || " + ((state[1]) ? 1 : 0);
	}
}
