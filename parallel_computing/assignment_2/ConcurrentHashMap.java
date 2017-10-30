import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentHashMap<Key, Value> {
	
	private volatile LinkedList<Key, Value>[] buckets;
	private volatile int size;
	private volatile int maxCapacity;
	private final Random rand = new Random();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public ConcurrentHashMap(int maxCapacity){
		this.maxCapacity = maxCapacity;
		buckets = new LinkedList[maxCapacity];
		size = 0;
		init();
	}

	private void init(){
		for (int i = 0; i < maxCapacity; i++){
			buckets[i] = new LinkedList<Key,Value>();
		}
	}

	public Value get(Key key) {
		if (!containsKey(key)){
			return null;
		}
		int index = hashCode(key);
		Node<Key, Value> tmp = null;
		lock.readLock().lock();
		try{
			tmp = buckets[index].get(key);
		} finally {
			lock.readLock().unlock();
			if (tmp == null){
				return null;
			}
			return tmp.getValue();
		}
	}

	public Value put(Key key, Value value){
		int index = hashCode(key);
		if (containsKey(key)){
			return get(key);
		}
		lock.writeLock().lock();
		try{
			buckets[index].addFirst(new Node<Key, Value>(index, key, value));
			size++;
			//buckets[index].getFirst().display();
		} finally {
			//Value retVal = buckets[index].getFirst().getValue();
			lock.writeLock().unlock();
			return null;
		}
	}

	public Value remove(Key key){
		int index = hashCode(key);
		if (!containsKey(key)){
			return null;
		}

		lock.writeLock().lock();
		Value retVal = null;
		try {
			retVal = buckets[index].removeByKey(key).getValue();
			if (retVal != null){
				size--;
			}
		} finally {
			lock.writeLock().unlock();
			//System.out.println("I HAVE GIVEN UP THE REMOVE LOCK");
			return retVal;
		}
	}

	public int hashCode(Key key){
		return (key.hashCode() % maxCapacity);
	}

	public boolean containsKey(Key key){
		boolean retVal = false;
		lock.readLock().lock();
		try {
			retVal = (buckets[hashCode(key)].containsKey(key));
		} finally {
			lock.readLock().unlock();
			return retVal;
		}
	}

	/*public boolean containsValue(Object value){
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getValue() == value){
				return true;
			}
		}
		return false;
	}*/

	public void clear(){
		lock.writeLock().lock();
		try {
			for (int i = 0; i < maxCapacity; i++){
				buckets[i].clear();
			}
		} finally {
			size = 0;
			lock.writeLock().unlock();
		}
	}

	public boolean isEmpty(){
		return (size == 0);
	}

	public int size(){
		return size;
	}

	public String toString(){
		lock.readLock().lock();
		String retVal = "";
		try {
			for (int i = 0; i < maxCapacity; i++){
				if (!(buckets[i].isEmpty())){
					buckets[i].display();
				}
			}
		} finally {
			lock.readLock().unlock();
			return retVal;
		}
	}
}
