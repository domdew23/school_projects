public class LinkedList<Key, Value> {
	/* Each link has a reference to another link in the list
	LinkedList has only reference to the first and last link added
	newest guy added to the list  (firstLink) */
	
	public Node<Key, Value> first;
	public Node<Key, Value> last;

	public LinkedList(){
		first = null;
		last = null;
	}

	public void addFirst(Node n){
		if (isEmpty()){
			last = n;
		} else {
			first.previous = n;
		}
		n.next = first; // push first over
		first = n; // put n at the head
	}

	public void addLast(Node<Key, Value> n){
		if (isEmpty()){
			first = n;
		} else {
			last.next = n;
			n.previous = last;
		}
		last = n;
	}

	public Node<Key, Value> removeFirst(){
		Node<Key, Value> n = first;

		if (!isEmpty()){
			first = first.next; // move first.next to the head
		} else {
			System.out.println("list is empty");
		}
		return n;
	}

	public Node<Key, Value> removeLast(){
		Node n = last;
		if (!isEmpty()){
			last = n.previous;
			last.next = null;
		} else {
			System.out.println("list is empty");
		}
		return n;
	}

	public Node<Key, Value> remove(Node<Key, Value> n){
		Node<Key, Value> current = first;
		Node<Key, Value> previous = first;

		while (current.getKey() != n.getKey()){
			if (current.next == null){
				return null;
			}
			previous = current;
			current = current.next;
		}

		if (current == first){
			// matched first
			first = first.next;
		} else {
			previous.next = current.next;
		}
		return current;
	}

	public Node<Key, Value> removeByKey(Key key){
		Node<Key, Value> n = get(key);
		Node<Key, Value> retVal = remove(n);
		return retVal;
	}
	public boolean isEmpty(){
		return (first == null);
	}

	public boolean contains(Node<Key,Value> n){
		return (!isEmpty() && containsKey(n.getKey()));
	}

	public boolean containsKey(Key key){
		//System.out.println("stuck in here:" + key + "\n");
		//display();
		return (!isEmpty() && get(key) != null);
	}

	public Node<Key, Value> get(Key key){
		Node<Key, Value> current = first;
		while (!(key.equals(current.getKey()))){
			if (current.next == null){
				return null;
			}
			current = current.next;
		}
		return current;
	}

	public void clear(){
		Node<Key, Value> n = first;
		while (n.next != null){
			remove(n);
			n = n.next;
		}
	}
	
	public Node<Key, Value> getFirst(){
		return first;
	}

	public Node<Key, Value> getLast(){
		return last;
	}

	public void display(){
		Node<Key, Value> n = first;
		while (n != null){
			n.display();
			n = n.next;
		}
	}
}
