import java.util.ArrayList;
import java.math.BigDecimal;

public class Machine<Input, Output> implements AtomicModel<Input, Output> {
	private int p; // number of parts for the machine to process
	private BigDecimal s; // time remaining to process first of the parts
	private BigDecimal t; // time it takes machine to finish a part
	private String name;
	private static Scheduler scheduler;
	private static Time currentTime;
	private ArrayList<Input> inputs;
	private ArrayList<Output> outputs;
	private ArrayList<Event> eventLog;
	private static int count = 0;

	public Machine(BigDecimal t, String name){
		this.name = name;
		this.t = t;
		this.p = 0;
		this.s = new BigDecimal("0.0");
		this.inputs = new ArrayList<Input>();
		this.outputs = new ArrayList<Output>();
		this.eventLog = new ArrayList<Event>();
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
		
		for (Output o : outputs){
			try {
				Event lastEvent = eventLog.get(eventLog.size() - 2);
				BigDecimal e = currentTime.getReal().subtract(lastEvent.time.getReal());				
				Event event = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", (AtomicModel) o).addParameter(e).addParameter(lambda()).build();	
				scheduler.put(event);
			} catch (ClassCastException e){
				System.out.println("Network output: " + (++count));
			}
		}

		if (p > 0){
			Event event = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();	
			scheduler.put(event);
		}
	}

	// delete old event that was set with t, replace it with new event set with s

	public void deltaExternal(BigDecimal e, int q){
		// e = elapsed time | q = # of parts
		Event event = null;
		if (p > 0){
			// busy
			p += q;
			s = s.subtract(e);

			for (Event ev : scheduler.getEvents()){
				if (ev == null){
					continue;
				}
				if (ev.model == this && ev.kind == "deltaInternal"){
					if (scheduler.remove(ev)){
						event = Event.builder(new Time(currentTime.getReal().add(s), 0), "deltaInternal", this).build();
					}
				}
			}
		} else {
			// idle
			p += q;
			s = t;
			event = Event.builder(new Time(currentTime.getReal().add(t), 0), "deltaInternal", this).build();	
		}
		scheduler.put(event);
	}

	public void deltaConfluent(int q){
		// executed when ne+w part arrives and part is completed simultaneously
		// should eject the completed part
		p += (q - 1);
		s = t;

		Event event = Event.builder(new Time(currentTime.getReal().add(timeAdvance()), 0), "deltaInternal", this).build();
		scheduler.put(event);
		for (Output o : outputs){
			try {
				Event e1 = Event.builder(new Time(currentTime.getReal(), 0), "deltaExternal", (AtomicModel) o).addParameter(new BigDecimal("0.0")).addParameter(lambda()).build();
				scheduler.put(e1);
				log(e1);
			} catch (ClassCastException e){
				System.out.println("Network output: " + (++count));
			}
		}
	}

	public BigDecimal timeAdvance(){
		if (p > 0){
			return s;
		} else {
			return new BigDecimal("9999999999.9999999");
		}
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
