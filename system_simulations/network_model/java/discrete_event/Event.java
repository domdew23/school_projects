import java.math.BigDecimal;

public class Event<Type> implements Comparable<Event>{
	public Time time;
	public String kind; // input or output
	public Type obj;
	public int q;
	public BigDecimal e;

	private Event(){
	}

	public void updateE(BigDecimal e){
		this.e = e;
	}

	public static<Type>EventBuilder builder(Time t, String k, Type obj){
		return new EventBuilder(t, k, obj);
	}

	public int compareTo(Event other){
		return time.getReal().compareTo(other.time.getReal());
	}

	public String toString(){
		return "Time: " + time.getReal() + " | Kind: " + kind + " | Object: " + obj + " | e: " + e + " | q: " + q;
	}

	public static class EventBuilder<Type>{
		private Time time;
		private String kind;
		private Type obj;
		private int q = 0;
		private BigDecimal e = new BigDecimal(0.0);

		public EventBuilder(Time t, String k, Type o){
			time = t;
			kind = k;
			obj = o;
		}

		public EventBuilder addParameter(int q){
			this.q = q;
			return this;
		}

		public EventBuilder addParameter(BigDecimal e){
			this.e = e;
			return this;
		}

		public Event build(){
			Event<Type>event = new Event<Type>();
			event.time = this.time;
			event.kind = this.kind;
			event.obj = this.obj;
			event.q = q;
			event.e = e;
			return event;
		}
	}
}