public class Scheduler {

	private int DEFAULT_MAX_SIZE = 50;
	private Event[] events;
	private int size;

	public Scheduler(int maxSize){
		events = new Event[maxSize + 1];
		size = 0;
	}

	public Scheduler(){
		events = new Event[DEFAULT_MAX_SIZE + 1];
		size = 0;
	}

	public boolean put(Event e){
		try {
			if (size == events.length - 1){
				reSize();
			}
			events[++size] = e;
			check(size);
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void reSize(){
		Event[] old = events;
		events = new Event[events.length * 2];
		System.arraycopy(old, 1, events, 1, size);
	}

	private int greater(int i, int j){
		return events[i].time.getReal().compareTo(events[j].time.getReal());
	}

	private void siftUp(int i, int j){
		Event tmp = events[i];
		events[i] = events[j];
		events[j] = tmp;
	}

	private void siftDown(int k){
		/* check it has a child - if so sift to the bottom of the heap */
		while(2 * k <= size){
			int j = 2 * k;
			if (j < size && greater(j, j + 1) == 1){
				j++;
			} 

			if (!(greater(k, j) == 1)){
				break;
			}

			siftUp(k, j);
			k = j;
		}
	}

	private void check(int k){
		/* check if root or greater than parent - if so swap with parent*/
		while (k > 1 && greater(k/2, k) == 1){
			siftUp(k, k/2);
			k /= 2;
		}
	}

	public boolean contains(Event e){
		if (isEmpty()){
			return false;
		}

		for (Event tmp : events){
			if (tmp == e){
				return true;
			}
		}
		return false;
	}

	public Event peek(){
		/* return the root element */
		if (isEmpty()){
			throw new NullPointerException("Cannot peek an empty list.");
		}
		return events[1];
	}

	public Event pull(){
		if (isEmpty()){
			throw new NullPointerException("Cannot poll an empty queue.");
		}

		/* swap root and bottom node - store root - sift root back to the bottom */
		siftUp(1, size);
		Event e = events[size--];
		events[size + 1] = null;
		siftDown(1);
		return e;
	}

	public boolean remove(Event e){
		if (isEmpty() || !contains(e)){
			throw new NullPointerException("Trying to remove event that does not exist.");
		}

		for (int i = 1; i < size + 1; i++){
			if (events[i] == e){
				siftUp(i, size);
				Event tmp = events[size--];
				events[size + 1] = null;
				siftDown(i);
				return true;
			}
		}
		return false;
	}

	public Event find(String kind, AtomicModel model){
		for (Event e : events){
			if (e != null && e.kind == kind && e.model == model){
				return e;
			}
		}
		return null;
	}

	public int size(){
		return size;
	}

	public boolean isEmpty(){
		return size == 0;
	}

	public Event[] getEvents(){
		return events;
	}

	public void checkMerge(){
		// merge once you reach that moment in time
		for (Event e : events){
			if (e != null && e != peek()){
				//System.out.println("Peek time: " + peek().time.getReal() + "| event time: " + e.time.getReal() + 
					//" peek model: " + peek().model.name() + " | e model: " + e.model.name());
				if (peek().time.getReal().compareTo(e.time.getReal()) == 0 && peek().model == e.model){
					merge(peek(), e);
				}	
			}
		}
	}

	/* never compare floating point values for equality - don't use floats */
	public void merge(Event one, Event two){
		remove(one);
		remove(two);

		int q;
		if (one.q != 0){
			q = one.q;
		} else if (two.q != 0){
			q = two.q;
		} else {
			throw new NullPointerException("Something went wrong while merging.");
		}

		Event event = Event.builder(one.time, "deltaConfluent", one.model).addParameter(q).build();
		put(event);
	}

	public String toString(){
		String retVal = "";
		for (int i = 1; i < size + 1; i++){
			retVal += "Event " + i + ": " + events[i].toString() + "\n";
		}
		return retVal;
	}
}