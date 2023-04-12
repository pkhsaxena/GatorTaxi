package com.cop5536;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GatorTaxi {

	RedBlackTree rbt = new RedBlackTree();
	MinHeap minHeap = new MinHeap();
	FileWriter writer;

	public GatorTaxi() throws IOException {
		writer = new FileWriter("output_file.txt");
	}

	public static void main(String[] args) throws Exception {

		checkFile(args);
		fileRead(args[0]);

	}

	public static void checkFile(String[] args) {
		if (args.length == 0) {
			System.out.println("Please provide a filename as a command line argument.");
			System.exit(1);
		}

		String filename = args[0];
		File file = new File(filename);

		if (!file.exists() || !file.canRead()) {
			System.out.println("Cannot read file: " + filename);
			System.exit(1);
		}
	}

	public static void fileRead(String filename) throws Exception {
		GatorTaxi taxi = new GatorTaxi();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\(");
				String functionName = parts[0];
				if (!"GetNextRide".equalsIgnoreCase(functionName)) {
					int[] argsAsInts;
					String[] arguments = parts[1].substring(0, parts[1].length() - 1).split(",");
					argsAsInts = new int[arguments.length];
					for (int i = 0; i < arguments.length; i++) {
						argsAsInts[i] = Integer.parseInt(arguments[i].trim());
					}

					if ("Insert".equalsIgnoreCase(functionName) && argsAsInts.length == 3) {
						taxi.insert(argsAsInts[0], argsAsInts[1], argsAsInts[2]);
					} else if ("UpdateTrip".equalsIgnoreCase(functionName) && argsAsInts.length == 2) {
						taxi.update(argsAsInts[0], argsAsInts[1]);
					} else if ("Print".equalsIgnoreCase(functionName) && argsAsInts.length == 1) {
						taxi.print(argsAsInts[0]);
					} else if ("Print".equalsIgnoreCase(functionName) && argsAsInts.length == 2) {
						taxi.print(argsAsInts[0], argsAsInts[1]);
					} else if ("CancelRide".equalsIgnoreCase(functionName) && argsAsInts.length == 1) {
						taxi.cancelRide(argsAsInts[0]);
					}
				}
				if ("GetNextRide".equalsIgnoreCase(functionName)) {
					taxi.getNextRide();
				}
			}
		} catch (IOException e) {
			taxi.writer.close();
			e.printStackTrace();
		}
	}

	void insert(int rideNumber, int rideCost, int tripDuration) throws Exception {
		Ride ride = new Ride(rideNumber, rideCost, tripDuration);
		boolean inserted = rbt.insert(ride);
		if (!inserted) {
			writer.write("Duplicate RideNumber\n");
			writer.close();
			Runtime.getRuntime().halt(-100);
		} else {
			minHeap.insert(ride);
		}
	}

	void getNextRide() throws Exception {
		// if minHeap is empty, there are no rides
		if (minHeap.minHeap.isEmpty()) {
			writer.write("No active ride requests\n");
			return;
		}
		Ride ride = minHeap.extractMin();
		rbt.delete(ride.rideNumber);
		int rideno = ride.rideNumber;
		int cost = ride.rideCost;
		int trip = ride.tripDuration;
		writer.write("(" + rideno + "," + cost + "," + trip + ")\n");
	}

	void cancelRide(int rideNumber) throws Exception {
		// find the ride from Red-Black tree
		int pos = rbt.delete(rideNumber);
		// if such a ride exists, delete it from the minHeap
		if (pos != Integer.MIN_VALUE) {
			minHeap.deleteRideByIndex(pos);
		}
	}

	void update(int rideNumber, int newTripDuration) throws Exception {
		Node node = rbt.search(rideNumber);
		if (node == null) {
			return;
		}
		Ride ride = node.ride;
		// cancel and request a new ride
		if (newTripDuration <= ride.tripDuration) {
			cancelRide(rideNumber);
			insert(ride.rideNumber, ride.rideCost, newTripDuration);
		}
		// cancel and request a new ride
		else if (ride.tripDuration < newTripDuration && newTripDuration <= 2 * ride.tripDuration) {
			cancelRide(rideNumber);
			insert(ride.rideNumber, ride.rideCost + 10, newTripDuration);
		}
		// just cancel the ride
		else if (newTripDuration > 2 * ride.tripDuration) {
			cancelRide(rideNumber);
		}
	}

	void print(int rideNumber) throws IOException {
		// search for the ride, if the node returned is null, print 0,0,0
		Node node = rbt.search(rideNumber);
		if (node == null) {
			writer.write("(0,0,0)\n");
		} else {
			int rideno = node.ride.rideNumber;
			int cost = node.ride.rideCost;
			int trip = node.ride.tripDuration;
			writer.write("(" + rideno + "," + cost + "," + trip + ")\n");
		}
	}

	void print(int min, int max) throws IOException {
		// search for the nodes within this range, is the list is empty, print 0,0,0
		List<Ride> rides = rbt.range(min, max);
		if (rides.isEmpty()) {
			rides.add(new Ride(0, 0, 0));
		}
		StringBuilder sb = new StringBuilder();
		for (Ride ride : rides) {
			int rideno = ride.rideNumber;
			int cost = ride.rideCost;
			int trip = ride.tripDuration;
			sb.append("(" + rideno + "," + cost + "," + trip + "),");
		}
		String string = sb.toString();
		writer.write(string.substring(0, string.length() - 1) + "\n");
	}

}
