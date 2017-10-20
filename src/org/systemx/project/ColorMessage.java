package org.systemx.project;

import java.awt.Color;
import java.util.Date;

import org.movsim.simulator.vehicles.Vehicle;

import fr.ifsttar.licit.simulator.agents.communication.messages.Message;

public class ColorMessage extends Message {

	Color color;
	public ColorMessage(Vehicle sender, Color colorObject) {
		super(sender);
		color = colorObject;
	}
	public Color getColor() {
		return color;
	}

	public ColorMessage(Vehicle sender, long expirationInMilliseconds) {
		super(sender, expirationInMilliseconds);
	}

	public ColorMessage(Vehicle sender, Date expiration) {
		super(sender, expiration);
	}

	public ColorMessage(Vehicle sender) {
		super(sender);
	}
	@Override
	public String toString() {
		return "ColorMessage [color=" + color + ", idSender=" + idSender
				+ ", laneSender=" + laneSender + ", positionSender="
				+ positionSender + ", time=" + time + "]";
	}
	
	
}
