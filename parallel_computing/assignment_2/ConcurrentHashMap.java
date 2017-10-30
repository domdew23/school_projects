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
			if (tmp == null){
				return null;
			}
			lock.readLock().unlock();
			return tmp.getValue();
		}
	}

	public Value put(Key key, Value value){
		int index = hashCode(key);
		//System.out.println("I AM WAITING ON THE ADD LOCK");
		lock.writeLock().lock();
		try{
			//System.out.println("I HAVE THE ADD LOCK");
			buckets[index].addFirst(new Node<Key, Value>(index, key, value));
			size++;
			//incSize();
			//buckets[index].getFirst().display();
		} finally {
			//Value retVal = buckets[index].getFirst().getValue();
			lock.writeLock().unlock();
			//System.out.println("I HAVE GIVEN UP THE ADD LOCK");
			return null;
		}
	}

	public Value remove(Key key){
		int index = hashCode(key);
		//System.out.println("I AM WAITING ON THE REMOVE LOCK");
		if (!containsKey(key)){
			return null;
		}

		Value retVal = null;
		lock.writeLock().lock();
		try {
			//System.out.println("I HAVE THE REMOVE LOCK");
			retVal = buckets[index].removeByKey(key).getValue();
			//decSize();
			if (retVal != null){
				size--;
			}
		} finally {

			lock.writeLock().unlock();

			//System.out.println("I HAVE GIVEN UP THE REMOVE LOCK");
			return retVal;
		}
		//System.out.println("Trying to remove a value that does not exist");	
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
		String retVal = "";
		for (int i = 0; i < maxCapacity; i++){
			if (!(buckets[i].isEmpty())){
				buckets[i].display();
			}
		}
		return retVal;
	}
}
