package fr.ifsttar.licit.simulator.agents.communication.messages;

import java.awt.Color;

import org.movsim.simulator.vehicles.Vehicle;

public class MessageColor extends Message {

	Color color;
	public MessageColor(Vehicle sender, Color colorObject) {
		super(sender);
		color = colorObject;
	}
	public Color getColor() {
		return color;
	}

}
