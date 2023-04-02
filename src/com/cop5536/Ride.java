package com.cop5536;

public class Ride {

	long rideNumber;
	int rideCost;
	int tripDuration;
	@Override
	public String toString() {
		return "Ride [rideNumber=" + rideNumber + "]";
	}

	int pos;

	public Ride(long rideNumber, int rideCost, int tripDuration) {
		super();
		this.rideNumber = rideNumber;
		this.rideCost = rideCost;
		this.tripDuration = tripDuration;
		this.pos = 0;
	}

}
