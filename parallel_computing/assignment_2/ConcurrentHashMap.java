import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentHashMap<Key, Value> {
	
	private LinkedList[] buckets;
	private int size;
	private int maxCapacity;
	private Random rand = new Random();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public ConcurrentHashMap(int maxCapacity){
		buckets = new LinkedList[maxCapacity];
		size = 0;
		this.maxCapacity = maxCapacity;
	}

	public Value get(Key key) {
		int index = hashCode(key);
		lock.readLock().lock();
		Node tmp;
		try{
			tmp = buckets[index].getFirst();
			while (tmp.getKey() != key){
				if (tmp.next == null){
					return null;
				}
				tmp = tmp.next;
			}
		} finally {
			lock.readLock().unlock();
		}
		return tmp.getValue();
	}

	public Value put(Key key, Value value){
		int index = hashCode(key);
		lock.writeLock().lock();
		try{
			buckets[index].addFirst(new Node<Key, Value>(index, key, value));
			size++;
		} finally {
			lock.writeLock().unlock();
			return nodes[index].getValue();
		}
	}

	public Value remove(Key key){
		int index = hashCode(key);
		lock.writeLock().lock();
		try {
			if (buckets[index].remove(key) != null){
				size--;
				return;
			}
		} finally {
			lock.writeLock().unlock();
		}
		//System.out.println("Trying to remove a value that does not exist");	
	}

	public int hashCode(Object o){
		return (o.hashCode() % maxCapacity);
	}

	public boolean containsKey(Object key){
		int index = hashCode(key);
		if (nodes[index] != null){
			return true;
		}

		return false;
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
