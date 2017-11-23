import java.util.ArrayList;
import java.math.BigDecimal;

public class Machine<Input, Output> implements AtomicModel<Input, Output> {
	
	private static Scheduler<Machine> scheduler;
	private static Time currentTime = new Time(new BigDecimal(0.0), 0);
	private static int count = 0;
	private BigDecimal s;
	private BigDecimal t;
	private ArrayList<Input> inputs;
	private ArrayList<Output> outputs;
	private ArrayList<Event> eventLog;
	private String name;
	private Time lastTime;
	private int p;

	public Machine(BigDecimal t, String name){
		this.name = name;
		this.t = t;
		this.p = 0;
		this.s = new BigDecimal(0.0);
		this.inputs = new ArrayList<Input>();
		this.outputs = new ArrayList<Output>();
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
		
		for (Output output : outputs){
			if (Network.class.isInstance(output)){
				System.out.println("Network output: " + (++count));
			} else {
				Event<Machine> event = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", output).addParameter(lambda()).build();	
				scheduler.put(event);	
			}
		}

		if (p > 0){
			Event<Machine> event = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();	
			scheduler.put(event);
		}
	}

	public void deltaExternal(BigDecimal e, int q){
		lastTime = currentTime;
		
		Event<Machine> event = null;
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

		Event<Machine> outputEvent = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();
		scheduler.put(outputEvent);

		for (Output output : outputs){
			if (Network.class.isInstance(output)){
				System.out.println("Network output: " + (++count));		
			} else {
				Event<Machine> inputEvent = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", output).addParameter(new BigDecimal("0.0")).addParameter(lambda()).build();
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
