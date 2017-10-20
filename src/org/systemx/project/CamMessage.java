package org.systemx.project;

import fr.ifsttar.licit.simulator.agents.communication.messages.Message;

public class CamMessage extends Message {
	
	long id;
	double position;
	int lane;
	double speed;
	double acceleration;

	public CamMessage(long id, double position, int lane, double speed, double acceleration) {
		super();
		this.id = id;
		this.position = position;
		this.lane = lane;
		this.speed = speed;
		this.acceleration = acceleration;
	}
	public long getId() {
		return id;
	}

	public double getPosition() {
		return position;
	}

	public int getLane() {
		return lane;
	}

	public double getSpeed() {
		return speed;
	}

	public double getAcceleration() {
		return acceleration;
	}

	
	
}
