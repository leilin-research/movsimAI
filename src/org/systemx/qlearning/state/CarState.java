package org.systemx.qlearning.state;

public class CarState {
	public double lane; // 1 - 1.5 - 2 - 2.5 - 3
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
	
	boolean matchesCarState(CarState carState) {
		if(position==carState.position && speed== carState.speed && lane==carState.lane ) {
			return true;
		}else {
			return false;
		}
	}
	
}
