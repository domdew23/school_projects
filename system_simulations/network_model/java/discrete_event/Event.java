public class Event {
	public Time time;
	public String kind;
	public String model;

	public Event(Time time, String kind, String model){
		this.time = time;
		this.kind = kind;
		this.model = model;
	}

	public String toString(){
		return "Time: " + time.getReal() + " | Kind: " + kind + " | Model: " + model;
	}
}