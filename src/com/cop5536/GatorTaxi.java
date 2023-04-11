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
//					System.out.println(functionName + " ");
//					for (int i = 0; i < argsAsInts.length; i++) {
//						System.out.print(argsAsInts[i] + " ");
//					}
//					System.out.println();
//					taxi.minHeap.printHeap();
//					taxi.rbt.prettyPrint();
//					System.out.println();
				}
				if ("GetNextRide".equalsIgnoreCase(functionName)) {
					taxi.getNextRide();
//					System.out.println(functionName + " ");
//					System.out.println();
//					taxi.minHeap.printHeap();
//					taxi.rbt.prettyPrint();
//					System.out.println();
//					 System.out.print(functionName);
				}
				// Do something with functionName and argsAsInts
//				System.out.println();
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
//			System.out.println("INSERT: Duplicate RideNumber");
			writer.write("Duplicate RideNumber\n");
			writer.close();
			Runtime.getRuntime().halt(-100);
		} else {
			minHeap.insert(ride);
//			System.out.println("INSERT: " + rideNumber);
		}
	}

	void getNextRide() throws Exception {
		if (minHeap.minHeap.isEmpty()) {
//			System.out.println("NEXT RIDE: No active ride requests");
			writer.write("No active ride requests\n");
			return;
		}
		Ride ride = minHeap.extractMin();
		try {
			rbt.delete(ride.rideNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int rideno = ride.rideNumber;
		int cost = ride.rideCost;
		int trip = ride.tripDuration;
//		System.out.println("NEXT RIDE: (" + rideno + "," + cost + "," + trip + ")");
		writer.write("(" + rideno + "," + cost + "," + trip + ")\n");
	}

	void cancelRide(int rideNumber) throws Exception {
		int pos = rbt.delete(rideNumber);
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
		if (newTripDuration <= ride.tripDuration) {
			cancelRide(rideNumber);
			insert(ride.rideNumber, ride.rideCost, newTripDuration);
		} else if (ride.tripDuration < newTripDuration && newTripDuration <= 2 * ride.tripDuration) {
			cancelRide(rideNumber);
			insert(ride.rideNumber, ride.rideCost + 10, newTripDuration);
		} else if (newTripDuration > 2 * ride.tripDuration) {
			cancelRide(rideNumber);
		}
	}

	void print(int rideNumber) throws IOException {
		Node node = rbt.search(rideNumber);
		if (node == null) {
//			System.out.println("PRINT: (0,0,0)");
			writer.write("(0,0,0)\n");
		} else {
			int rideno = node.ride.rideNumber;
			int cost = node.ride.rideCost;
			int trip = node.ride.tripDuration;
//			System.out.println("PRINT: (" + rideno + "," + cost + "," + trip + ")");
			writer.write("(" + rideno + "," + cost + "," + trip + ")\n");
		}
	}

	void print(int min, int max) throws IOException {
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
//		System.out.println(string.substring(0, string.length() - 2));
//		writer.write("print 2 args\n");
//		System.out.println("print 2 args");
	}

}
