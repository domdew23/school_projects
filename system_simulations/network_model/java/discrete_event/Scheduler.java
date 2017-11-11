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

	private boolean greater(int i, int j){
		return events[i].time.getReal() > events[j].time.getReal();
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
			if (j < size && greater(j, j + 1)){
				j++;
			} 

			if (!greater(k, j)){
				break;
			}

			siftUp(k, j);
			k = j;
		}
	}

	private void check(int k){
		/* check if root or greater than parent - if so swap with parent*/
		while (k > 1 && greater(k/2, k)){
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
		siftDown(1);
		return e;
	}

	public boolean remove(Event e){
		if (isEmpty() || !contains(e)){
			throw new NullPointerException("Trying to remove event that does not exist.");
		}

		for (int i = 0; i < size; i++){
			if (events[i] == e){
				if (events[i+1] != null){
					events[i] = events[i+1];
					events[i+1] = null; 
				} else {
					events[i] = null;
				}
				return true;
			}
		}
		return false;
	}

	public int size(){
		return size;
	}

	public boolean isEmpty(){
		return size == 0;
	}

	public void checkMerge(){
		// merge once you reach that moment in time
		for (Event e : events){
			if (peek().time.getReal() == e.time.getReal() && peek().model == e.model){
				merge(peek(), e);
			}
		}
	}

	/* never compare floating point values for equality - don't use floats */
	public void merge(Event one, Event two){
		remove(one);
		remove(two);
		Event event = Event.builder(one.time, "deltaConfluent", one.model).build();
		put(event);
	}

	public String toString(){
		String retVal = "";
		for (int i = 1; i < size+1; i++){
			retVal += "Event " + i + ": " + events[i].toString() + "\n";
		}
		return retVal;
	}
}