package com.cop5536;

public class Ride {

	int rideNumber;
	int rideCost;
	int tripDuration;

	int pos;

	public Ride(int rideNumber, int rideCost, int tripDuration) {
		this.rideNumber = rideNumber;
		this.rideCost = rideCost;
		this.tripDuration = tripDuration;
		this.pos = 0;
	}

	public Ride(int rideNumber) {
		this.rideNumber = rideNumber;
	}

	@Override
	public String toString() {
		return "Ride [rideNumber=" + rideNumber + "]";
	}

}
