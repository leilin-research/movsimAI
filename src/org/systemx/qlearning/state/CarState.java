package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarState {
	public int lane; // 1 - 1.5 - 2 - 2.5 - 3
	public int position; // -100 100
	public int speed; // 0 20

	public CarState() {
		super();
		this.lane = 0;
		this.position = 0;
		this.speed = 0;
	}

	public CarState(CarState carState) {
		super();
		this.lane = carState.lane;
		this.position = carState.position;
		this.speed = carState.speed;
	}

	public CarState(int lane, int position, int speed) {
		super();
		this.lane = lane;
		this.position = position;
		this.speed = speed;
	}

	boolean matchesCarState(CarState carState) {
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

	public List<CarState> possibleAdj(int numberOfLanes, int speedLimit, boolean main) {
		List<CarState> list = new ArrayList<CarState>();
		if(!main) {
			list.add(new CarState(lane, position + 1, speed));
			list.add(new CarState(lane, position - 1, speed));
		}
		if(speed<speedLimit) {
			list.add(new CarState(lane, position, speed+1));
		}
		if(speed>0) {			
			list.add(new CarState(lane, position, speed-1));
		}

		return list;
	}

}
