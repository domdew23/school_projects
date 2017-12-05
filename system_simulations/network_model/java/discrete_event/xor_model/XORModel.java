/* Leaf */
import java.util.ArrayList;
import java.math.BigDecimal;

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
		this.bits = new ArrayList<Boolean>(2);
	}

	public boolean lambda(){
		return state;
	}

	public void deltaExternal(boolean b){
		if (bits.size() >= 2){
			bits.clear();
			bits.add(b);
			scheduler.remove(scheduler.find("deltaInternal", this));
			BigDecimal t = currentTime.getReal().add(timeAdvance());
			Event<AtomicModel> event = Event.builder(new Time(t, 0), "deltaInternal", this).build();
			scheduler.put(event);
		} else {
			bits.add(b);
			Event removedEvent = scheduler.remove(scheduler.find("deltaInternal", this));
			if (removedEvent != null){
				Event<AtomicModel> event = Event.builder(removedEvent.time, "deltaInternal", this).build();
				scheduler.put(event);
			} else {
				BigDecimal t = currentTime.getReal().add(timeAdvance());
				Event<AtomicModel> event = Event.builder(new Time(t, 0), "deltaInternal", this).build();
				scheduler.put(event);
			}
		}
	}

	public void deltaInternal(){
		state = (bits.get(0) ^ bits.get(1));
		bits.clear();
		for (O out : outputs){
			if (Network.class.isInstance(out)){
				System.out.println("Network output: " + (lambda() ? 1 : 0));
				return;
			}
			AtomicModel<I,O> o = (AtomicModel) out;
			for (I in : o.getInputs()){
				AtomicModel i = (AtomicModel) in;
				Event<AtomicModel> event = null;
				if (i == this){
					event = Event.builder(currentTime, "deltaExternal", o).addParameter(lambda()).build();
				} else {
					event = Event.builder(currentTime, "deltaExternal", i).addParameter(o.lambda()).build();
				}
				scheduler.put(event);
			}
		}
	}

	public void deltaConfluent(boolean x){
		bits.add(x);
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

	public BigDecimal timeAdvance(){
		return new BigDecimal(1.0);
	}

	public String toString(){
		int bitOne = -1;
		int bitTwo = -1;
		if (bits.size() == 1){
			bitOne = ((bits.get(0)) ? 1 : 0);
		}
		if (bits.size() == 2){
			bitOne = ((bits.get(0)) ? 1 : 0);
			bitTwo = ((bits.get(1)) ? 1 : 0);
		}
		return ("XOR" + id + " State: " + ((state) ? 1 : 0) + " | bits: (" + bitOne + "," + bitTwo + ")");
	}

}