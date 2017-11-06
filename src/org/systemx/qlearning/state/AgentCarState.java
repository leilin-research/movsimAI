package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentCarState {
	public int lane; // 1 - 1.5 - 2 - 2.5 - 3
	public int speed; // 0 20

	public AgentCarState() {
		super();
		this.lane = 0;
		this.speed = 0;
	}

	public AgentCarState(AgentCarState carState) {
		super();
		this.lane = carState.lane;
		this.speed = carState.speed;
	}

	public AgentCarState(int lane, int speed) {
		super();
		this.lane = lane;
		this.speed = speed;
	}

	boolean matchesCarState(AgentCarState carState) {
		if (speed == carState.speed && lane == carState.lane) {
			return true;
		} else {
			return false;
		}
	}


	public String serialiseValue() {
		return lane + ":" + speed;
	}

	public void parseValues(String myCarString) {
		String[] parts = myCarString.split(":");
		String lane = parts[0];
		String speed = parts[1];

		this.lane = Integer.parseInt(lane);
		this.speed = Integer.parseInt(speed);
	}

	public List<AgentCarState> possibleAdj(int numberOfLanes, int speedLimit) {
		List<AgentCarState> list = new ArrayList<AgentCarState>();
		if(speed<speedLimit) {
			list.add(new AgentCarState(lane, speed+1));
		}
		if(speed>0) {			
			list.add(new AgentCarState(lane, speed-1));
		}
		return list;
	}

}
