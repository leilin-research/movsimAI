package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdjacentCarState {
	public int lane; // 1 - 1.5 - 2 - 2.5 - 3
	public int position; // -100 100
	public int speed; // 0 20

	public AdjacentCarState() {
		super();
		this.lane = 0;
		this.position = 0;
		this.speed = 0;
	}

	public AdjacentCarState(AdjacentCarState carState) {
		super();
		this.lane = carState.lane;
		this.position = carState.position;
		this.speed = carState.speed;
	}

	public AdjacentCarState(int lane, int position, int speed) {
		super();
		this.lane = lane;
		this.position = position;
		this.speed = speed;
	}

	boolean matchesCarState(AdjacentCarState carState) {
		if (position == carState.position && speed == carState.speed && lane == carState.lane) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "CarState [lane=" + lane + ", position=" + position + ", speed=" + speed + "]";
	}

	public String serialiseValue() {
		return lane + ":" + position + ":" + speed;
	}

	public void parseValues(String myCarString) {
		String[] parts = myCarString.split(":");
		String lane = parts[0];
		String position = parts[1];
		String speed = parts[2];

		this.lane = Integer.parseInt(lane);
		this.position = Integer.parseInt(position);
		this.speed = Integer.parseInt(speed);
	}

	public List<AdjacentCarState> possibleAdj(int numberOfLanes, int speedLimit) {
		List<AdjacentCarState> list = new ArrayList<AdjacentCarState>();
		list.add(new AdjacentCarState(lane, position + 1, speed));
		list.add(new AdjacentCarState(lane, position - 1, speed));
		if (speed < speedLimit) {
			list.add(new AdjacentCarState(lane, position, speed + 1));
		}
		if (speed > 0) {
			list.add(new AdjacentCarState(lane, position, speed - 1));
		}

		return list;
	}

}
