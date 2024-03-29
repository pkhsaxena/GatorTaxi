package com.cop5536;

public class Node {
	Ride ride;
	int key;
	Color color;
	Node parent;
	Node leftChild;
	Node rightChild;

	public Node(int key) {
		super();
		this.key = key;
		this.color = Color.RED;
	}

	public Node(Ride ride) {
		super();
		this.ride = ride;
		this.key = ride.rideNumber;
		this.color = Color.RED;
	}

	@Override
	public String toString() {
		return "Node [rideNumber=" + key + ", color=" + color + "]";
	}

	public enum Color {
		RED, BLACK;
	}

	static Color nodesColor(Node node) {
		return node != null ? node.color : Color.BLACK;
	}

	static void setColor(Node node, Color color) {
		if (node != null) {
			node.color = color;
		}
	}

	static Node leftChildOfNode(Node node) {
		return node != null ? node.leftChild : null;
	}

	static Node rightChildOfNode(Node node) {
		return node != null ? node.rightChild : null;
	}

	static Node parentOfNode(Node node) {
		return node != null ? node.parent : null;
	}
}
