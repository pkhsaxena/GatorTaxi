package com.cop5536;

import static com.cop5536.Node.getColor;
import static com.cop5536.Node.leftChild;
import static com.cop5536.Node.parent;
import static com.cop5536.Node.rightChild;
import static com.cop5536.Node.setColor;

import java.util.Arrays;

import com.cop5536.Node.Color;

public class RedBlackTree {

	Node root;
	boolean debug = false;

	public static void main(String[] args) throws Exception {
		RedBlackTree rbt = new RedBlackTree();
		Arrays.asList(50, 40, 60, 30, 45, 70, 65, 80, 75, 85).forEach(no -> {
			try {
				rbt.insert(new Ride(no));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
//		rbt.prettyPrint();
		rbt.delete(50);
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
			System.out.println(root.key + "(" + sColor + ")");
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
		if (root != null && root.key == node.key || (root.leftChild != null && root.leftChild.key == node.key)
				|| (root.rightChild != null && root.rightChild.key == node.key)) {
			// duplicate ride number, as per specification this is an error
			throw new Exception("duplicate ride number " + node.key);
		}
		// if tree is not empty check where to insert to the new node
		// trivial case - only root is present in the tree
		if (root.leftChild == null && node.key < root.key) {
			root.leftChild = node;
			node.parent = root;
		} else if (root.rightChild == null && node.key > root.key) {
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
			if (node.key < current.key) {
				current = current.leftChild;
			} else if ((node.key > current.key)) {
				current = current.rightChild;
			} else {
				// duplicate ride number, as per specification this is an error
				throw new Exception("duplicate ride number " + node.key);
			}
		}

		node.parent = parent;
		if (node.key < parent.key) {
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

		while (node != null && node.parent != null && node.parent.color == Color.RED) {
			// check color of parent's sibling for XXr condition
			// L
			if (node.parent == node.parent.parent.leftChild) {
				Node sibling = node.parent.parent.rightChild;

				if (sibling != null && sibling.color == Color.RED) {
					sibling.color = Color.BLACK;
					node.parent.color = Color.BLACK;
					node.parent.parent.color = Color.RED;
					node = node.parent.parent;
				} else {
					if (node == node.parent.rightChild) {
						// LR rotation
						LR(node.parent.parent);
						node.leftChild.color = Color.RED;
						node.color = Color.BLACK;
						node.rightChild.color = Color.RED;
						node = node.parent;
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
					node = node.parent.parent;
				} else {
					if (node == node.parent.leftChild) {
						// RL rotation
						RL(node.parent.parent);
						node.leftChild.color = Color.RED;
						node.color = Color.BLACK;
						node.rightChild.color = Color.RED;
						node = node.parent;
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
		debug("LR");
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
		debug("RL");
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
		if (gp != null) {
			debug("LL");
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
	}

	private void RR(Node gp) {

		if (gp != null) {
			debug("RR");
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

	public void delete(int rideNumber) {
		// first find the node with this rideNumber
		Node nodeToDelete = null;
		Node current = this.root;
		while (current != null) {
			if (current.key == rideNumber) {
				nodeToDelete = current;
			}
			if (rideNumber < current.key) {
				current = current.leftChild;
			} else {
				current = current.rightChild;
			}
		}

		if (nodeToDelete == null) {
			System.out.println("node with given rideNumber not found " + rideNumber);
			return;
		}

		// check if we need to perform fixes
		/*
		 * if neither child is null, the node is of degree 2. we need to replace it with
		 * the minimum of the right subtree
		 */
//		boolean fix = nodeToDelete.color == Color.BLACK;
//		Node rootOfDeficientSubtree = null;
//		if (nodeToDelete.leftChild != null && nodeToDelete.rightChild != null) {
//			Node minNode = min(nodeToDelete.rightChild);
//			// update flag according to the color of the node being deleted
//			fix = minNode.color == Color.BLACK;
//			// since the minNode cannot have a left child
//			// the root of deficient subtree must be the right child
//			rootOfDeficientSubtree = minNode.rightChild;
//			// replace minNode with right child
//			swap(minNode, minNode.rightChild);
//			// now minNode is not related to any node in the tree
//			// use minNode to replace the nodeWith data
//			minNode.rightChild = nodeToDelete.rightChild;
//			swap(nodeToDelete, minNode);
//			if (nodeToDelete.rightChild != null) {
//				nodeToDelete.rightChild.parent = minNode;
//			}
//			minNode.leftChild = nodeToDelete.leftChild;
//			minNode.leftChild.parent = minNode;
//			minNode.color = nodeToDelete.color;
//			// now node to delete is free
//			System.out.println("swap 3");
//		}
//		/*
//		 * degree 1 checks for the node with the data we need to delete if either left
//		 * or right child is null, the node is a degree 1 node the other non-null child
//		 * will be the root of the deficient subtree
//		 */
//		else if (nodeToDelete.leftChild == null) {
//			rootOfDeficientSubtree = nodeToDelete.rightChild;
//			System.out.println("swap 1");
//			swap(nodeToDelete, rootOfDeficientSubtree);
//		} else {
//			rootOfDeficientSubtree = nodeToDelete.leftChild;
//			swap(nodeToDelete, rootOfDeficientSubtree);
//			System.out.println("swap 2");
//		}
//
//		if (fix) {
//			System.out.println("fixing");
//			fixDelete(rootOfDeficientSubtree);
//		}
		if (nodeToDelete.leftChild != null && nodeToDelete.rightChild != null) {
			Node s = min(nodeToDelete.rightChild);
			nodeToDelete.key = s.key;
			nodeToDelete.ride = s.ride;
			nodeToDelete = s;
		} // p has 2 children

		// Start fixup at replacement node, if it exists.
		Node replacement = (nodeToDelete.leftChild != null ? nodeToDelete.leftChild : nodeToDelete.rightChild);

		if (replacement != null) {
			// Link replacement to parent
			replacement.parent = nodeToDelete.parent;
			if (nodeToDelete.parent == null)
				root = replacement;
			else if (nodeToDelete == nodeToDelete.parent.leftChild)
				nodeToDelete.parent.leftChild = replacement;
			else
				nodeToDelete.parent.rightChild = replacement;

			// Null out links so they are OK to use by fixAfterDeletion.
			nodeToDelete.leftChild = nodeToDelete.rightChild = nodeToDelete.parent = null;

			// Fix replacement
			if (nodeToDelete.color == Color.BLACK)
				fixDelete(replacement);
		} else if (nodeToDelete.parent == null) { // return if we are the only node.
			root = null;
		} else { // No children. Use self as phantom replacement and unlink.
			if (nodeToDelete.color == Color.BLACK)
				fixDelete(nodeToDelete);

			if (nodeToDelete.parent != null) {
				if (nodeToDelete == nodeToDelete.parent.leftChild)
					nodeToDelete.parent.leftChild = null;
				else if (nodeToDelete == nodeToDelete.parent.rightChild)
					nodeToDelete.parent.rightChild = null;
				nodeToDelete.parent = null;
			}
		}
	}

	private void fixDelete(Node node) {
		prettyPrint();
		int i = 0;
		while (node != root && getColor(node) == Color.BLACK) {
			debug("iter = " + i++);
			if (node == leftChild(parent(node))) {
				Node sibling = rightChild(parent(node));
				// case 1
				if (getColor(sibling) == Color.RED) {
					setColor(sibling, Color.BLACK);
					setColor(parent(node), Color.RED);
					RR(parent(node));
					// update sibling of the node
					sibling = rightChild(parent(node));
				}

				// sibling is black
				// case 2
				if (getColor(leftChild(sibling)) == Color.BLACK && getColor(rightChild(sibling)) == Color.BLACK) {
					setColor(sibling, Color.RED);
					node = parent(node);
				} else {
					// case 3
					if (getColor(rightChild(sibling)) == Color.BLACK) {
						setColor(leftChild(sibling), Color.BLACK);
						setColor(sibling, Color.RED);
						LL(sibling);
						sibling = rightChild(parent(node));
					}
					// case 3 is always followed by case 4, no need for else condition
					// case 4
					setColor(sibling, getColor(parent(node)));
					setColor(rightChild(sibling), Color.BLACK);
					setColor(parent(node), Color.BLACK);
					RR(parent(node));
					// once case 4 is executed, we always have a red-black tree
					node = root;
				}
			} else { // similar to if condition
				Node sibling = leftChild(parent(node));
				// case 1
				if (getColor(sibling) == Color.RED) {
					setColor(sibling, Color.BLACK);
					setColor(parent(node), Color.RED);
					RR(parent(node));
					// update sibling of the node
					sibling = leftChild(parent(node));
				}

				// sibling is black
				// case 2
				if (getColor(leftChild(sibling)) == Color.BLACK && getColor(rightChild(sibling)) == Color.BLACK) {
					setColor(sibling, Color.RED);
					node = parent(node);
				} else {
					// case 3
					if (getColor(rightChild(sibling)) == Color.BLACK) {
						setColor(rightChild(sibling), Color.BLACK);
						setColor(sibling, Color.RED);
						RR(sibling);
						sibling = leftChild(parent(node));
					}
					// case 3 is always followed by case 4, no need for else condition
					// case 4
					setColor(sibling, getColor(parent(node)));
					setColor(parent(node), Color.BLACK);
					setColor(leftChild(sibling), Color.BLACK);
					LL(parent(node));
					// once case 4 is executed, we always have a red-black tree
					node = root;
				}
			}

		}
		if (node != null) {
			node.color = Color.BLACK;
		}
	}

	private Node min(Node node) {
		while (node.leftChild != null) {
			node = node.leftChild;
		}
		return node;
	}

	private Node max(Node node) {
		while (node.rightChild != null) {
			node = node.rightChild;
		}
		return node;
	}

	private void swap(Node parent, Node child) {
		if (parent.parent == null) {
			this.root = child;
		} else if (parent == parent.parent.leftChild) {
			parent.parent.leftChild = child;
		} else {
			parent.parent.rightChild = child;
		}
		if (child != null) {
			child.parent = parent.parent;
		}
	}

	void debug(String msg) {
		if (debug) {
			System.out.println(msg);
		}
	}
}
