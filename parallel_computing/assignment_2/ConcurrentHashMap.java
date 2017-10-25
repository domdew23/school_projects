import java.util.Random;

public class ConcurrentHashMap<Key, Value> {
	
	private Node<Key, Value>[] nodes;
	private LinkedList[] buckets;
	private int size;
	private int maxCapacity;
	private Random rand = new Random();

	public ConcurrentHashMap(int maxCapacity){
		nodes = new Node[maxCapacity];
		buckets = new LinkedList[maxCapacity];
		size = 0;
		this.maxCapacity = maxCapacity;
	}

	public Value get(Key key) {
		int index = hashCode(key);
		System.out.println(" getting: " + index + "...");
		while (nodes[index] == null){
			index = rand.nextInt(size);
		}
		return nodes[index].getValue();
	}

	public Value put(Key key, Value value){
		int index = hashCode(key);
		System.out.println(" adding: " + index + "...");
		//System.out.println("hash: " + index);
		if (nodes[index] == null){
			nodes[index] = new Node<Key, Value>(index, key, value);
			size++;
			return nodes[index].getValue();
		}

		System.out.println("THERE HAS BEEN A COLLISION. Consider adding a linkedlist as buckets");
		return nodes[index].getValue();
	}

	public void remove(Key key){
		int index = hashCode(key);
		System.out.println(" removing: " + index + "...");
		if (nodes[index] != null){
			nodes[index] = null;
			size--;
			return;
		}
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

	public boolean containsValue(Object value){
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getValue() == value){
				return true;
			}
		}
		return false;
	}

	public void clear(){
		for (int i = 0; i < maxCapacity; i++){
			remove(nodes[i].getKey());
		}
	}

	public boolean isEmpty(){
		if (size == 0){
			return true;
		}
		return false;
	}

	public int size(){
		return size;
	}

	public String toString(){
		String retVal = "";
		for (int i = 0; i < maxCapacity; i++){
			if (nodes[i] != null){
				retVal += ("My index (hash): " + nodes[i].hashCode() + " || My id: " + nodes[i].getKey() + "\n");
			}
		}
		return retVal;
	}
}