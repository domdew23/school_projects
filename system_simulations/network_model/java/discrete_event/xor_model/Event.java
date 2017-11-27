import java.math.BigDecimal;

public class Event<T> implements Comparable<Event<T>>{
	public Time time;
	public String kind; // input or output
	public T obj;
	public boolean[] X;

	private Event(){
	}


	public static<T>EventBuilder builder(Time t, String k, T o){
		return new EventBuilder<T>(t, k, o);
	}

	public int compareTo(Event<T> other){
		int retVal = (time.getReal() > other.time.getReal()) ? 1 : 0;
		return retVal;
	}

	public String toString(){
		return "Time: " + time.getReal() + " | Kind: " + kind + " | Object: " + obj + " | X: " + X[0] + "," + X[1];
	}

	public static class EventBuilder<T>{
		private Time time;
		private String kind;
		private T obj;
		private boolean[] X;

		public EventBuilder(Time t, String k, T o){
			time = t;
			kind = k;
			obj = o;
			X = new boolean[2];
		}

		public EventBuilder addParameter(boolean[] X){
			this.X = X;
			return this;
		}

		public EventBuilder addParameter(boolean x){
			X[1] = x;
			return this;
		}

		public EventBuilder addParameter(boolean b1, boolean b2){
			X[0] = b1;
			X[1] = b2;
			return this;
		}

		public Event build(){
			Event<T> event = new Event<T>();
			event.time = this.time;
			event.kind = this.kind;
			event.obj = this.obj;
			event.X = X;
			return event;
		}
	}
}