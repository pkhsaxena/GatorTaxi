package com.cop5536;

public class Node {
	Ride ride;
	Color color;
	Node leftChild;
	Node rightChild;
	Node parent;

	public Node(Ride ride) {
		super();
		this.ride = ride;
		this.color = Color.RED;
	}

	public enum Color {
		RED, BLACK;
	}
}
