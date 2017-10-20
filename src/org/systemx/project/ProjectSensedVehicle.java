package org.systemx.project;
import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class ProjectSensedVehicle extends  SensedVehicle{

	private double senderPosition;
	private int senderLane;
	private double senderSpeed;
	private double senderAcceleration;

	public ProjectSensedVehicle(long senderId, double senderPosition, int senderLane,
			double senderSpeed, double senderAcceleration) {
		super(senderId);
		this.senderPosition = senderPosition;
		this.senderLane = senderLane;
		this.senderSpeed = senderSpeed;
		this.senderAcceleration = senderAcceleration;
	}
	
	public double getSenderAcceleration() {
		return senderAcceleration;
	}

	public double getSenderPosition() {
		return senderPosition;
	}

	public int getSenderLane() {
		return senderLane;
	}
	
	public double getSenderSpeed() {
		return senderSpeed;
	}

	@Override
	public String toString() {
		return "ProjectSensedVehicle [senderPosition=" + senderPosition + ", senderLane=" + senderLane
				+ ", senderSpeed=" + senderSpeed + ", senderAcceleration=" + senderAcceleration + "]";
	}

	
	
}
