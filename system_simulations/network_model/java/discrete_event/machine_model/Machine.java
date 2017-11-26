import java.util.ArrayList;
import java.math.BigDecimal;

public class Machine<I,O> implements AtomicModel<I,O> {
	
	private static Scheduler<AtomicModel> scheduler;
	private static Time currentTime = new Time(new BigDecimal(0.0), 0);
	private static int count = 0;
	private BigDecimal s;
	private BigDecimal t;
	private ArrayList<I> inputs;
	private ArrayList<O> outputs;
	private ArrayList<Event> eventLog;
	private String name;
	private Time lastTime;
	private int p;

	public Machine(BigDecimal t, String name){
		this.name = name;
		this.t = t;
		this.p = 0;
		this.s = new BigDecimal(0.0);
		this.inputs = new ArrayList<I>();
		this.outputs = new ArrayList<O>();
		this.eventLog = new ArrayList<Event>();
		this.lastTime = new Time(new BigDecimal(0.0), 0);
	}

	public int lambda(){
		return 1;
	}

	public void deltaInternal(){
		lastTime = currentTime;
		p--;
		s = t;
		
		for (O output : outputs){
			if (Network.class.isInstance(output)){
				System.out.println("Network output: " + (++count));
			} else {
				Event<AtomicModel> event = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", output).addParameter(lambda()).build();	
				scheduler.put(event);	
			}
		}

		if (p > 0){
			Event<AtomicModel> event = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();	
			scheduler.put(event);
		}
	}

	public void deltaExternal(BigDecimal e, int q){
		lastTime = currentTime;
		
		Event<AtomicModel> event = null;
		if (p > 0){
			p += q;
			s = s.subtract(e);

			if(scheduler.remove(scheduler.find("deltaInternal", this))){
				event = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();
			}

		} else {
			p += q;
			s = t;
			event = Event.builder(new Time(currentTime.getReal().add(t), 0), "deltaInternal", this).build();	
		}
		scheduler.put(event);
	}

	public void deltaConfluent(int q){
		lastTime = currentTime;
		p += (q - 1);
		s = t;

		Event<AtomicModel> outputEvent = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();
		scheduler.put(outputEvent);

		for (O output : outputs){
			if (Network.class.isInstance(output)){
				System.out.println("Network output: " + (++count));		
			} else {
				Event<AtomicModel> inputEvent = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", output).addParameter(new BigDecimal("0.0")).addParameter(lambda()).build();
				scheduler.put(inputEvent);		
			}	
		}
	}

	public BigDecimal timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return new BigDecimal(Integer.MAX_VALUE);
		}
	}

	public Time getLast(){
		return lastTime;
	}

	public void log(Event e){
		eventLog.add(e);
	}

	public void addInput(I I){
		inputs.add(I);
	}

	public void addOutput(O O){
		outputs.add(O);
	}

	public static void setTime(Time t){
		currentTime = t;
	}

	public static Time getTime(){
		return currentTime;
	}

	public static BigDecimal getRealTime(){
		return currentTime.getReal();
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
		return name + ": | p: " + p + " | s: " + s;
	}
}
