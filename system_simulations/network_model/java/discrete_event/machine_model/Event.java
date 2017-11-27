import java.math.BigDecimal;

public class Event<T> implements Comparable<Event<T>>{
	public Time time;
	public String kind; // input or output
	public T obj;
	public int q;
	public BigDecimal e;

	private Event(){
	}

	public void updateE(BigDecimal e){
		this.e = e;
	}

	public static<T>EventBuilder builder(Time t, String k, T o){
		return new EventBuilder<T>(t, k, o);
	}

	public int compareTo(Event<T> other){
		return time.getReal().compareTo(other.time.getReal());
	}

	public String toString(){
		return "Time: " + time.getReal() + " | Kind: " + kind + " | Object: " + obj + " | e: " + e + " | q: " + q;
	}

	public static class EventBuilder<T>{
		private Time time;
		private String kind;
		private T obj;
		private int q = 0;
		private BigDecimal e = new BigDecimal(0.0);

		public EventBuilder(Time t, String k, T o){
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
			Event<T> event = new Event<T>();
			event.time = this.time;
			event.kind = this.kind;
			event.obj = this.obj;
			event.q = q;
			event.e = e;
			return event;
		}
	}
}