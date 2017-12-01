/* Leaf */
import java.util.ArrayList;

public class XORModel<I,O> implements AtomicModel<I,O>{
	private boolean state;
	private static int count = 0;
	public static Time currentTime;
	public static Scheduler<AtomicModel> scheduler;
	public static Network<AtomicModel> network;
	private int id;
	private ArrayList<I> inputs;
	private ArrayList<O> outputs;
	private ArrayList<Boolean> bits;

	public XORModel(){
		this.state = false;
		this.id = ++count;
		this.inputs = new ArrayList<I>();
		this.outputs = new ArrayList<O>();
		this.bits = new ArrayList<Boolean>();
	}

	public boolean lambda(){
		System.out.println("XOR" + id + " Output: " + ((state) ? 1 : 0));
		return state;
	}

	/*public void delta(boolean[] X){
		System.out.println("XOR" + id + " Input: " + ((X[0]) ? 1 : 0) + ((X[1]) ? 1 : 0));
		state = (X[0] ^ X[1]);
		System.out.println("XOR" + id + " State: " + ((state) ? 1 : 0) + "\n==================");
	}*/

	public void deltaExternal(boolean b){
		// state = (X[0] ^ X[1]);
		// has map of inputs
		// memory model creates events for itself
		System.out.println("size: " + bits.size());
		if (bits.size() >= 2){
			// interrupt
			bits.clear();
			bits.add(b);
			scheduler.remove(scheduler.find("deltaInternal", this));
			double t = currentTime.getReal() + timeAdvance();
			Event<AtomicModel> event = Event.builder(new Time(t, 0), "deltaInternal", this).build();
			scheduler.put(event);
		} else {
			bits.add(b);
			Event removedEvent = scheduler.remove(scheduler.find("deltaInternal", this));

			if (removedEvent != null){
				Event<AtomicModel> event = Event.builder(removedEvent.time, "deltaInternal", this).build();
				scheduler.put(event);
			} else {
				double t = currentTime.getReal() + timeAdvance();
				Event<AtomicModel> event = Event.builder(new Time(t, 0), "deltaInternal", this).build();
				scheduler.put(event);
			}
		}
	}

	public void deltaInternal(){
		state = (bits.get(0) ^ bits.get(1));
		bits.clear();
		// if both bits are not there by the time this executes have an error message for the user
		/*boolean[] X = new boolean[2];
		for (O out : outputs){
			if (network.getOutputs().contains(this)){
				System.out.println("Network output: " + lambda());
				AtomicModel o = (AtomicModel) out;
				AtomicModel one = (AtomicModel) o.getInputs().get(0);
				X[0] = one.lambda();
				Event<AtomicModel> event = Event.builder(currentTime, "deltaExternal", out).addParameter(X[0]).build();
				scheduler.put(event);
				return;
			}
			AtomicModel o = (AtomicModel) out;
			AtomicModel one = (AtomicModel) o.getInputs().get(0);
			AtomicModel two = (AtomicModel) o.getInputs().get(1);
			X[0] = one.lambda();
			X[1] = two.lambda();
			Event<AtomicModel> event = Event.builder(currentTime, "deltaExternal", out).addParameter(X).build();
			scheduler.put(event);
		}*/
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
		return ("XOR" + id + " State: " + ((state) ? 1 : 0));
	}

}