package com.cop5536;

public class Node {
	Ride ride;
	Color color;
	Node parent;
	Node leftChild;
	Node rightChild;

	@Override
	public String toString() {
		return "Node [ride=" + ride + ", color=" + color + "]";
	}

	public Node() {
	}

	public Node(Ride ride) {
		super();
		this.ride = ride;
		this.color = Color.RED;
	}

	public enum Color {
		RED, BLACK;
	}
}
