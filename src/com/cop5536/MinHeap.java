package com.cop5536;

import java.util.ArrayList;
import java.util.List;

public class MinHeap {

	List<Ride> minHeap;

	public MinHeap() {
		minHeap = new ArrayList<>();
	}

	public void insert(Ride rideToInsert) {

		// first append the value to the list for safe keeping
		minHeap.add(rideToInsert);
		int currentIndex = rideToInsert.pos = minHeap.size() - 1;

		// swap while the current node is not root and the parent of ride cost is
		// greater than current node ride cost, or parent node trip duration is longer
		// than current node trip duration (given ride cost is same)
		while (currentIndex > 0 && minHeap.get(parent(currentIndex)).rideCost >= minHeap.get(currentIndex).rideCost) {
			if ((minHeap.get(parent(currentIndex)).rideCost == minHeap.get(currentIndex).rideCost
					&& minHeap.get(parent(currentIndex)).tripDuration > minHeap.get(currentIndex).tripDuration)
					|| minHeap.get(parent(currentIndex)).rideCost > minHeap.get(currentIndex).rideCost) {
				swap(currentIndex, parent(currentIndex));
			}
			currentIndex = parent(currentIndex);
		}

	}

	public Ride extractMin() throws Exception {
		// nothing to remove
		if (minHeap.isEmpty()) {
			throw new Exception("Cannot remove min, Heap is empty");
		}
		Ride root = getRoot();
		if (minHeap.size() == 1) {
			minHeap.clear();
			return root;
		}
		// replace root with right most child of the minHeao
		minHeap.set(0, minHeap.remove(minHeap.size() - 1));
		// correct position of the element
		minHeap.get(0).pos = 0;
		fixHeap(0);
		return root;
	}

	private void fixHeap(int i) {
		int minIndex = i;
		// left child index
		int lci = 2 * i + 1;
		// right child index
		int rci = 2 * i + 2;

		// check left child
		if (lci < minHeap.size() && ((minHeap.get(lci).rideCost < minHeap.get(minIndex).rideCost)
				|| (minHeap.get(lci).rideCost == minHeap.get(minIndex).rideCost
						&& minHeap.get(lci).tripDuration < minHeap.get(minIndex).tripDuration))) {
			minIndex = lci;

		}
		// check right child
		if (rci < minHeap.size() && ((minHeap.get(rci).rideCost < minHeap.get(minIndex).rideCost)
				|| (minHeap.get(rci).rideCost == minHeap.get(minIndex).rideCost
						&& minHeap.get(rci).tripDuration < minHeap.get(minIndex).tripDuration))) {
			minIndex = rci;

		}

		// if either the left or right child are less than current node, swap and do a
		// recursion
		if (i != minIndex) {
			swap(i, minIndex);
			fixHeap(minIndex);
		}
	}

	/*
	 * CancelRide(rideNumber) deletes the triplet (rideNumber, rideCost,
	 * tripDuration) from the data structures, can be ignored if an entry for
	 * rideNumber doesn’t exist.
	 */
	public void deleteRideByIndex(int pos) throws Exception {
		if (pos == 0) {
			extractMin();
			return;
		}
		if (pos >= minHeap.size() || pos < 0 || minHeap.isEmpty()) {
			throw new Exception("element at given index does not exist " + pos + " heap size = " + minHeap.size());
		}
		// move the delete operation down the heap
		minHeap.set(pos, minHeap.get(minHeap.size() - 1));
		minHeap.get(pos).pos = pos;
		minHeap.remove(minHeap.size() - 1);
		fixHeap(0);
	}

	private void swap(int child, int parent) {
		Ride temp = minHeap.get(child);
		minHeap.set(child, minHeap.get(parent));
		minHeap.set(parent, temp);

		// after swapping update the pos attribute to the index of the node in the
		// minHeap array
		minHeap.get(child).pos = child;
		minHeap.get(parent).pos = parent;
	}

	private int parent(int i) {
		return (i - 1) / 2;
	}

	public Ride getRoot() {
		return minHeap.get(0);
	}

	public void printHeap() {
		int level = 0;
		int nodesInLevel = 1;
		for (int i = 0; i < minHeap.size(); i++) {
			if (i == nodesInLevel - 1) {
//	            System.out.println();
				level++;
				nodesInLevel += (1 << level);
			}
			System.out.println("index = " + i + " rideNumber = " + minHeap.get(i).rideNumber + " rideCost = "
					+ minHeap.get(i).rideCost + " tripDuration = " + minHeap.get(i).tripDuration);
		}
		System.out.println();
	}

//	public static void main(String[] args) throws Exception {
//		MinHeap minHeap = new MinHeap();
//		minHeap.insert(new Ride(1, 10, 30));
//		minHeap.insert(new Ride(2, 20, 20));
//		minHeap.insert(new Ride(3, 10, 20));
//		minHeap.insert(new Ride(4, 15, 25));
//		minHeap.insert(new Ride(5, 5, 40));
//
//		minHeap.printHeap();
//
//		minHeap.deleteRideByIndex(1);
//		minHeap.printHeap();
//
////		System.out.println(minHeap.extractMin().rideNumber); // Expected output: 5
////		minHeap.printHeap();
////		System.out.println(minHeap.extractMin().rideNumber); // Expected output: 1
////		minHeap.printHeap();
////		System.out.println(minHeap.extractMin().rideNumber); // Expected output: 3
////		minHeap.printHeap();
////		System.out.println(minHeap.extractMin().rideNumber); // Expected output: 2
////		minHeap.printHeap();
////		System.out.println(minHeap.extractMin().rideNumber); // Expected output: 4
////		minHeap.printHeap();
//
//	}

}
