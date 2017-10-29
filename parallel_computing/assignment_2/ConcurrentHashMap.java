import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentHashMap<Key, Value> {
	
	private volatile LinkedList<Key, Value>[] buckets;
	private volatile int size;
	private volatile int maxCapacity;
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
		int index = hashCode(key);
		lock.readLock().lock();
		Node<Key, Value> tmp = null;
		try{
			tmp = buckets[index].get(key);
		} finally {
			lock.readLock().unlock();
			return tmp.getValue();
		}
	}

	public Value put(Key key, Value value){
		int index = hashCode(key);
		lock.writeLock().lock();
		try{
			buckets[index].addFirst(new Node<Key, Value>(index, key, value));
			size++;
		} finally {
			Value retVal = buckets[index].getFirst().getValue();
			lock.writeLock().unlock();
			return retVal;
		}
	}

	public Value remove(Key key){
		int index = hashCode(key);
		lock.writeLock().lock();
		Value retVal = null;
		try {
			retVal = buckets[index].removeByKey(key).getValue();
			if (retVal != null){
				size--;
			}
		} finally {
			lock.writeLock().unlock();
			return retVal;
		}
	}

	public int hashCode(Key key){
		return (key.hashCode() % maxCapacity);
	}

	public boolean containsKey(Key key){
		lock.readLock().lock();
		boolean retVal = false;
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
		for (int i = 0; i < maxCapacity; i++){
			buckets[i].clear();
		}
	}

	public boolean isEmpty(){
		return (size == 0);
	}

	public int size(){
		return size;
	}

	public String toString(){
		String retVal = "";
		for (int i = 0; i < maxCapacity; i++){
			if (!(buckets[i].isEmpty())){
				buckets[i].display();
			}
		}
		return retVal;
	}
}
