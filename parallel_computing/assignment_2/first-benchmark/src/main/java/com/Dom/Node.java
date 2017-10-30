package com.Dom;

public class Node<Key, Value> {
	private int hash;
	private Key key;
	private Value value;
	public Node<Key, Value> next;
	public Node<Key, Value> previous;
	
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
		return false;
	}

	public void display(){
		System.out.println("Key: " + key + " | Hash: " + hash + " | Value: " + value + " | Next: " + next);
	}
}
