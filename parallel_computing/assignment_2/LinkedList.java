public class LinkedList {
	/* Each link has a reference to another link in the list
	LinkedList has only reference to the first and last link added
	newest guy added to the list  (firstLink) */
	
	public Node first;
	public Node last;

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

	public void addLast(Node n){
		if (isEmpty()){
			first = n;
		} else {
			last.next = n;
			n.previous = last;
		}
		last = n;
	}

	public Node removeFirst(){
		Node n = first;

		if (!isEmpty()){
			first = first.next; // move first.next to the head
		} else {
			System.out.println("list is empty");
		}
		return n;
	}

	public Node removeLast(){
		Node n = last;
		if (!isEmpty()){
			last = n.previous;
			last.next = null;
		} else {
			System.out.println("list is empty");
		}
		return n;
	}

	public Node remove(Node n){
		Node current = first;
		Node previous = first;

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

	public boolean isEmpty(){
		return (first == null);
	}

	public boolean contains(Node n){
		if (isEmpty()){
			return false;
		}

		Node tmp = first;
		while (n.getKey() != tmp.getKey()){
			if (tmp.next == null){
				return false;
			}
			tmp = tmp.next;
		}
		return true;
	}

	public Node getFirst(){
		return first;
	}

	public Node getLast(){
		return last;
	}

	public void display(){
		Node n = first;
		while (n != null){
			n.display();
			n = n.next;
		}
	}
}