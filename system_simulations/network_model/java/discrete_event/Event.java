public class Event {
	public Time time;
	public String kind; // input or output
	public AtomicModel model;
	public int q;
	public double e;

	private Event(){
	}

	public static EventBuilder builder(Time t, String k, AtomicModel m){
		return new EventBuilder(t, k, m);
	}

	public String toString(){
		return "Time: " + time.getReal() + " | Kind: " + kind + " | Model: " + model.name();
	}

	public static class EventBuilder {
		private Time time;
		private String kind;
		private AtomicModel model;
		private int q;
		private double e;

		public EventBuilder(Time t, String k, AtomicModel m){
			time = t;
			kind = k;
			model = m;
			q = -1;
			e = -1;
		}

		public EventBuilder addParameter(int q){
			this.q = q;
			return this;
		}

		public EventBuilder addParameter(double e){
			this.e = e;
			return this;
		}

		public Event build(){
			Event event = new Event();
			event.time = this.time;
			event.kind = this.kind;
			event.model = this.model;
			event.q = q;
			event.e = e;
			return event;
		}
	}
}