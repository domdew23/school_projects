public class Node<Key, Value> implements Map.Entry<Key, Value>{
	final private int hash;
	final private Key key;
	final private Value value;

	public Node(int hash, Key key, Value value){
		this.hash = hash;
		this.key = key;
		this.value = value;
	}

	public Key getKey(){
		return key;
	}

	public Value getValue(){
		return value;
	}

	public int hashCode(){
		return hash;
	}

	public Value setValue(Value value){
		Value tmp = this.value;
		this.value = value;
		return tmp;
	}

	public boolean equals(Object o){
		boolean sameKey = false;
		boolean sameValue = false;
		boolean sameHase = false;
	}
}