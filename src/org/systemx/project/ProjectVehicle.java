package org.systemx.project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Speed;
import org.movsim.simulator.roadnetwork.Lanes;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.lanechange.LaneChangeModel;
import org.movsim.simulator.vehicles.lanechange.LaneChangeModel.LaneChangeDecision;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.IDM;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.LongitudinalModelBase;
import org.systemx.project.control.Controller;
import org.systemx.project.control.VehicleControl;

import fr.ifsttar.licit.simulator.agents.communication.messages.MeasureMessage;
import fr.ifsttar.licit.simulator.agents.communication.messages.Message;
import fr.ifsttar.licit.simulator.agents.perception.MeasurementPerception;
import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;
import fr.ifsttar.licit.simulator.agents.perception.sensors.vehicles.measurements.GPSMeasurement;
import fr.ifsttar.licit.simulator.agents.perception.sensors.vehicles.measurements.Measurement;
import fr.ifsttar.licit.simulator.agents.perception.sensors.vehicles.measurements.TelemeterMeasurement;
import fr.ifsttar.licit.simulator.agents.perception.vehicles.VehicleSelfPerception;

public class ProjectVehicle extends Vehicle {

	/*
	 * Constructor
	 */

	/*
	 * used contructor.
	 */
	static Controller controller;
	LaneChangeDecision manualLaneChange = LaneChangeDecision.NONE;

	public ProjectVehicle(String label, LongitudinalModelBase accelerationModel,
			VehiclePrototypeConfiguration configuration, LaneChangeModel laneChangeModel) {
		super(label, accelerationModel, configuration, laneChangeModel);
		if(controller == null) {
			controller = new Controller();
		}
		controller.checkNewVehicle(this.getId());
	}

	public ProjectVehicle(Vehicle vehicle) {
		super(vehicle);
		if(controller == null) {
			controller = new Controller();
		}
		controller.checkNewVehicle(this.getId());
	}

	public ProjectVehicle(double roadLength, double d, int laneNumber, double e, double f) {
		super(roadLength, d, laneNumber, e, f);
		if(controller == null) {
			controller = new Controller();
		}
		controller.checkNewVehicle(this.getId());
	}

	SensedVehicle immediateLeader = null;

	/**
	 * handles received messages
	 * 
	 */
	@Override
	public void handleMessages(double simulationTime) {

		// read all previously received messages
		while (this.getMailSize() > 0) {
			// pick the next message from queued message
			Message message = this.pickMessage();
			// check if the message has not expired
			
			if (!message.isOutDated()) {
				// deal with measure messages (shared sensor information from
				// surrounding cooperative vehicles)

				if (message instanceof CamMessage) {

					long senderId = ((CamMessage) message).getId();
					double senderPosition = ((CamMessage) message).getPosition();
					int senderLane = ((CamMessage) message).getLane();
					double senderSpeed = ((CamMessage) message).getSpeed();
					double senderAcceleration = ((CamMessage) message).getAcceleration();

					ProjectSensedVehicle sv = new ProjectSensedVehicle(senderId, senderPosition, senderLane,
							senderSpeed, senderAcceleration);
					getCommunicatingVehicles().put(sv.getSenderId(), sv);
				}
			}
		}

		resetMessagesToSend();
	}

	/*
	 * Vehicle decide of its behavior (non-Javadoc)
	 * 
	 * @see fr.ifsttar.licit.simulator.agents.Agent#makeDecision(double, long)
	 */

	@Override
	public void makeDecision(double simulationTime, long iterationCount) {

		// get perception of itself
		VehicleSelfPerception selfPerception = new VehicleSelfPerception(this);

		double myNewSpeed = selfPerception.getSpeed();
		for (SensedVehicle sv : selfPerception.getCommunicationVehicles().values())
			myNewSpeed += sv.getAbsoluteV();

		myNewSpeed = myNewSpeed / (selfPerception.getCommunicationVehicles().size() + 1);

		if (selfPerception.getCommunicationVehicles().isEmpty()) {
			setColorObject(Color.black);
		} else {
			float intensity = selfPerception.getCommunicationVehicles().size();
			if (intensity > 5) {
				intensity = 5;
			}
			intensity = intensity / 5;
			setColorObject(Color.getHSBColor((float) 0.4705882352941176, (float) 1.0, (float) intensity));
		}

		sendMessage(new CamMessage(getId(), getFrontPosition(), getLane(), getSpeed(), getAcc()));

		controller.controlDecision(this);
	}

	public void modifyDesiredSpeed(double ns) {
		if (this.longitudinalModel instanceof IDM) {
			IDM IDMModel = (IDM) this.longitudinalModel;
			IDMModel.setDesiredSpeed(ns);

		}
	}

	public double getDesiredSpeed() {
		if (this.longitudinalModel instanceof IDM) {
			IDM IDMModel = (IDM) this.longitudinalModel;
			return IDMModel.getDesiredSpeed();
		}
		return Double.NaN;
	}

	/*
	 * change its desired lane
	 */
	public void modifiedDesiredLane(BehaviorEnum behavior) {
		switch (behavior) {
		case leftToright:
			setManualLaneChange(LaneChangeDecision.MANDATORY_TO_RIGHT);
			break;
		case rightToleft:
			setManualLaneChange(LaneChangeDecision.MANDATORY_TO_LEFT);
			break;
		}
	}

	public LaneChangeDecision getManualLaneChange() {
		return manualLaneChange;
	}

	public void setManualLaneChange(LaneChangeDecision manualLaneChange) {
		this.manualLaneChange = manualLaneChange;
	}
	
	public void vehicleCrashed(long id) {
		controller.checkVehicleCrash(id);
	}

}
