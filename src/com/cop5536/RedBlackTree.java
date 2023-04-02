package com.cop5536;

import com.cop5536.Node.Color;

public class RedBlackTree {

	Node root;

	public static void main(String[] args) throws Exception {
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert(new Ride(5, 21, 1));
		rbt.insert(new Ride(2, 20, 2));
		rbt.insert(new Ride(10, 30, 3));
		rbt.insert(new Ride(1, 30, 3));
		rbt.insert(new Ride(4, 30, 3));
		rbt.insert(new Ride(6, 30, 3));
		rbt.insert(new Ride(12, 30, 3));
		rbt.insert(new Ride(3, 30, 3));
		rbt.insert(new Ride(7, 30, 3));
		rbt.insert(new Ride(13, 30, 3));
		rbt.prettyPrint();
	}

	public void prettyPrint() {
		printHelper(this.root, "", true);
	}

	private void printHelper(Node root, String indent, boolean last) {
		// print the tree structure on the screen
		if (root != null) {
			System.out.print(indent);
			if (last) {
				System.out.print("R----");
				indent += "     ";
			} else {
				System.out.print("L----");
				indent += "|    ";
			}

			String sColor = root.color == Color.RED ? "RED" : "BLACK";
			System.out.println(root.ride.rideNumber + "(" + sColor + ")");
			printHelper(root.leftChild, indent, false);
			printHelper(root.rightChild, indent, true);
		}
	}

	public void insert(Ride ride) throws Exception {
		Node node = new Node(ride);
		// check if tree is empty
		if (root == null) {
			root = node;
			root.color = Color.BLACK;
			return;
		}
		if (root != null && root.ride.rideNumber == node.ride.rideNumber
				|| (root.leftChild != null && root.leftChild.ride.rideNumber == node.ride.rideNumber)
				|| (root.rightChild != null && root.rightChild.ride.rideNumber == node.ride.rideNumber)) {
			// duplicate ride number, as per specification this is an error
			throw new Exception("duplicate ride number " + node.ride.rideNumber);
		}
		// if tree is not empty check where to insert to the new node
		// trivial case - only root is present in the tree
		if (root.leftChild == null && node.ride.rideNumber < root.ride.rideNumber) {
			root.leftChild = node;
			node.parent = root;
		} else if (root.rightChild == null && node.ride.rideNumber > root.ride.rideNumber) {
			root.rightChild = node;
			node.parent = root;
		}
		if (node.parent == root) {
			return;
		}
		// trivial cases checked
		// node will be inserted somewhere in the left or right subtrees
		Node parent = null;
		Node current = root;
		// find a node such that parent of the node as space for a child
		while (current != null) {
			parent = current;
			if (node.ride.rideNumber < current.ride.rideNumber) {
				current = current.leftChild;
			} else if ((node.ride.rideNumber > current.ride.rideNumber)) {
				current = current.rightChild;
			} else {
				// duplicate ride number, as per specification this is an error
				throw new Exception("duplicate ride number " + node.ride.rideNumber);
			}
		}

		node.parent = parent;
		if (node.ride.rideNumber < parent.ride.rideNumber) {
			parent.leftChild = node;
		} else {
			parent.rightChild = node;
		}

		// since this node is not root node, may need to check double red condition
		if (node.parent.parent == null) {
			// this is a level 2 node, no need to check
			return;
		}
		checkAndFixInsert(node);
	}

	// travel back up using the parent pointers till root to check double red
	// condition
	private void checkAndFixInsert(Node node) {
		if (node.parent.color == Color.BLACK) {
			return;
		}
		// since Red-Black tree guarantee only one rotation to balance the tree
		if (node.parent.color == Color.RED) {
			// check color of parent's sibling for XXr condition
			// L
			if (node.parent == node.parent.parent.leftChild) {
				Node sibling = node.parent.parent.rightChild;

				if (sibling != null && sibling.color == Color.RED) {
					sibling.color = Color.BLACK;
					node.parent.color = Color.BLACK;
					node.parent.parent.color = Color.RED;
				} else {
					if (node == node.parent.rightChild) {
						// LR rotation
						LR(node.parent.parent);
						node.leftChild.color = Color.RED;
						node.color = Color.BLACK;
						node.rightChild.color = Color.RED;
					} else {
						// LL rotation
						node.parent.color = Color.BLACK;
						node.parent.parent.color = Color.RED;
						LL(node.parent.parent);
					}
				}
			} else {
				Node sibling = node.parent.parent.leftChild;

				if (sibling != null && sibling.color == Color.RED) {
					sibling.color = Color.BLACK;
					node.parent.color = Color.BLACK;
					node.parent.parent.color = Color.RED;
				} else {
					if (node == node.parent.leftChild) {
						// RL rotation
						RL(node.parent.parent);
						node.leftChild.color = Color.RED;
						node.color = Color.BLACK;
						node.rightChild.color = Color.RED;
					} else {
						// RR rotation
						node.parent.color = Color.BLACK;
						node.parent.parent.color = Color.RED;
						RR(node.parent.parent);
					}
				}
			}
			// keep the root node as black
			root.color = Color.BLACK;
		}
	}

	private void LR(Node gp) {
		Node p = gp.leftChild;
		Node n = p.rightChild;

		p.rightChild = n.leftChild;
		if (n.leftChild != null) {
			n.leftChild.parent = p;
		}
		n.leftChild = p;
		p.parent = n;

		gp.leftChild = n.rightChild;
		if (n.rightChild != null) {
			n.rightChild.parent = gp;
		}
		n.rightChild = gp;

		n.parent = gp.parent;
		if (gp.parent == null) {
			this.root = n;
		} else if (gp == gp.parent.leftChild) {
			gp.parent.leftChild = n;
		} else {
			gp.parent.rightChild = n;
		}
		gp.parent = n;

	}

	private void RL(Node gp) {
		Node p = gp.rightChild;
		Node n = p.leftChild;

		p.leftChild = n.rightChild;
		if (n.rightChild != null) {
			n.rightChild.parent = p;
		}
		n.rightChild = p;
		p.parent = n;

		gp.rightChild = n.leftChild;
		if (n.leftChild != null) {
			n.leftChild.parent = gp;
		}
		n.leftChild = gp;

		n.parent = gp.parent;
		if (gp.parent == null) {
			this.root = n;
		} else if (gp == gp.parent.leftChild) {
			gp.parent.leftChild = n;
		} else {
			gp.parent.rightChild = n;
		}
		gp.parent = n;

	}

	private void LL(Node gp) {
		Node p = gp.leftChild;

		gp.leftChild = p.rightChild;
		if (p.rightChild != null) {
			p.rightChild.parent = gp;
		}
		p.rightChild = gp;

		p.parent = gp.parent;
		if (gp.parent == null) {
			this.root = p;
		} else if (gp == gp.parent.leftChild) {
			gp.parent.leftChild = p;
		} else {
			gp.parent.rightChild = p;
		}
		gp.parent = p;
	}

	private void RR(Node gp) {
		Node p = gp.rightChild;

		gp.rightChild = p.leftChild;
		if (p.leftChild != null) {
			p.leftChild.parent = gp;
		}
		p.leftChild = gp;

		p.parent = gp.parent;
		if (gp.parent == null) {
			this.root = p;
		} else if (gp == gp.parent.leftChild) {
			gp.parent.leftChild = p;
		} else {
			gp.parent.rightChild = p;
		}
		gp.parent = p;
	}

}
