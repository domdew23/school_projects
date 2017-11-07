public class Scheduler {

	private Event[] events;
	private int size;

	public Scheduler(int maxSize){
		events = new Event[maxSize];
		size = 0;
	}

	public boolean put(Event e){
		// need to put event with newest time on the top
		try {
			events[++size] = e;
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
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
		if (isEmpty()){
			throw new NullPointerException("Cannot peek an empty list.");
		}
		return events[size];
	}

	public Event pull(){
		// take event with the oldest time from the bottom
		if (isEmpty()){
			throw new NullPointerException("Cannot poll an empty queue.");
		}

		Event e = peek();
		events[--size] = null;
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

	public boolean checkMerge(){
		return false;
	}

	public void merge(){
		
	}
}