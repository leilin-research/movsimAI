package org.systemx.project;
import java.awt.Color;

import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class ProjectSensedVehicle extends  SensedVehicle{

	private Color senderColor;
	private double senderPosition;
	private int senderLane;
	
	public Color getSenderColor() {
		return senderColor;
	}

	public double getSenderPosition() {
		return senderPosition;
	}

	@Override
	public String toString() {
		return "ProjectSensedVehicle [senderColor=" + senderColor
				+ ", senderPosition=" + senderPosition + ", senderLane="
				+ senderLane + "]";
	}

	public int getSenderLane() {
		return senderLane;
	}

	public ProjectSensedVehicle(long senderId) {
		super(senderId);
	}
	
	public ProjectSensedVehicle(long senderId, Color senderColor, 
			double senderPosition, int senderLane){
		this(senderId);
		this.senderColor = senderColor;
		this.senderLane = senderLane;
		this.senderPosition = senderPosition;
	}
	
}
