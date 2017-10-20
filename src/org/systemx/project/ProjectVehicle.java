package org.systemx.project;

import java.awt.Color;
import java.util.ArrayList;

import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.simulator.roadnetwork.Lanes;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.lanechange.LaneChangeModel;
import org.movsim.simulator.vehicles.lanechange.LaneChangeModel.LaneChangeDecision;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.IDM;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.LongitudinalModelBase;
import org.systemx.vehiclecontrol.vehicleControl;

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

	static vehicleControl vc;
	static boolean vcActive = false;
	static long vcid;

	LaneChangeDecision manualLaneCange = LaneChangeDecision.NONE;

	public ProjectVehicle(String label, LongitudinalModelBase accelerationModel,
			VehiclePrototypeConfiguration configuration, LaneChangeModel laneChangeModel) {
		super(label, accelerationModel, configuration, laneChangeModel);
		if (!vcActive) {
			vcid = getId();
			vc = new vehicleControl();
			vcActive = true;
		}
	}

	public ProjectVehicle(Vehicle vehicle) {
		super(vehicle);
		if (!vcActive) {
			vcid = getId();
			vc = new vehicleControl();
			vcActive = true;
		}
	}

	public ProjectVehicle(double roadLength, double d, int laneNumber, double e, double f) {
		super(roadLength, d, laneNumber, e, f);
		if (!vcActive) {
			vcid = getId();
			vc = new vehicleControl();
			vcActive = true;
		}
	}

	/*
	 * Initial Scenario
	 */
	public void scenarioSlowVehicles() {
		/*
		 * vehicles stay on their lane and their desired speed is divided by 2
		 */
		modifyDesiredSpeed(-0.5);
	}

	public void scenarioSlowVehiclesOnRight() {
		/*
		 * vehicles change to right if they are on left and their desired speed is
		 * divided by 2
		 */
		modifyDesiredSpeed(-0.5);
		modifiedDesiredLane(BehaviorEnum.leftToright);
	}

	public void scenarioSlowVehiclesOnleft() {
		/*
		 * vehicles change to right if they are on left and their desired speed is
		 * divided by 2
		 */
		modifyDesiredSpeed(-0.5);
		modifiedDesiredLane(BehaviorEnum.rightToleft);
	}

	SensedVehicle immediateLeader = null;

	/**
	 * handles received messages
	 * 
	 */
	@Override
	public void handleMessages(double simulationTime) {

		MeasurementPerception measurementPerception = (MeasurementPerception) this.perception;

		ArrayList<SensedVehicle> surroundingVehicles = new ArrayList<SensedVehicle>();

		double myPosition = this.getFrontPosition();
		// double myRearPosition = this.getRearPosition();
		double mySpeed = this.getSpeed();

		// add own measurements
		for (Measurement m : measurementPerception.getOwnMeasurements()) {
			// look for GPS positioning
			if (m instanceof GPSMeasurement) {
				GPSMeasurement gpsM = (GPSMeasurement) m;
				myPosition = gpsM.getPositionValue();
				mySpeed = gpsM.getSpeedValue();
				// look for telemeter measure
			} else if (m instanceof TelemeterMeasurement) {
				TelemeterMeasurement teleM = (TelemeterMeasurement) m;

				immediateLeader = new SensedVehicle(this.getId(), teleM.getDeltaXValue(), teleM.getDeltaVValue(), 0.0,
						0.0, teleM.getDeltaXValue(), teleM.getDeltaVValue());

				surroundingVehicles.add(new SensedVehicle(this.getId(), teleM.getDeltaXValue(), teleM.getDeltaVValue(),
						0.0, 0.0, teleM.getDeltaXValue(), teleM.getDeltaVValue()));
			}
		}

		// read all previously received messages
		while (this.getMailSize() > 0) {
			// pick the next message from queued message
			Message message = this.pickMessage();
			// check if the message has not expired
			if (!message.isOutDated()) {
				// deal with measure messages (shared sensor information from
				// surrounding cooperative vehicles)

				if (message instanceof ColorMessage) {
					Color c = ((ColorMessage) message).getColor();
					int lane = ((ColorMessage) message).getLaneSender();
					double position = ((ColorMessage) message).getPositionSender();
					ProjectSensedVehicle sv = new ProjectSensedVehicle(message.getIdSender(), c, position, lane);
					getCommunicatingVehicles().put(sv.getSenderId(), sv);
				}

				// if (message instanceof MeasureMessage) {
				//
				// // cast the message to get more precise information
				// MeasureMessage measureM = (MeasureMessage) message;
				//
				// // get embedded data
				// final double absoluteX = measureM.getPositionMeasureValue();
				// final double deltaX = measureM.getDeltaXMeasureValue();
				// final double deltaV = measureM.getVelocityMeasureValue();
				//
				// // check if the vehicle looks forward or backward
				// double localDeltaX = 0.0;
				//
				// localDeltaX = absoluteX - myPosition + deltaX;
				//
				// final double epsilon = this.getLength();
				//
				// // if (Math.abs(localDeltaX) > epsilon) {
				// double localDeltaV = Math.abs(measureM
				// .getVelocityMeasureValue() - mySpeed)
				// + measureM.getDeltaVMeasureValue();
				//
				// if (frontVehicleId == message.getIdSender()) {
				// immediateLeader.setSenderId(frontVehicleId);
				// immediateLeader.setAbsoluteX(absoluteX);
				// immediateLeader.setAbsoluteV(measureM.getVelocityMeasureValue());
				// }
				//
				// SensedVehicle sv = new SensedVehicle(message.getIdSender(), localDeltaX,
				// localDeltaV,
				// absoluteX, measureM.getVelocityMeasureValue(),
				// deltaX, deltaV);
				// getCommunicatingVehicles().put(sv.getSenderId(),sv);
				//
				// }
			}
		}

		measurementPerception.setSurroundingVehicles(surroundingVehicles);
		// position of the function has be validated
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
			// setColorObject(Color.getHSBColor((float)0.4705882352941176,(float)
			// 1.0,(float)intensity));
			setColorObject(Color.getHSBColor((float) 0.4705882352941176, (float) 1.0, (float) intensity));
		}
		sendMessage(new ColorMessage(this, Color.black));

		if (vcid == getId()) {
			if (getDistanceToRoadSegmentEnd() < 30) {
				vcActive = false;
			} else {
				setColorObject(Color.red);

				if (vc.acceleration == 1) {
					if (getSpeed() < 20) {
						modifyDesiredSpeed2(-Double.MAX_VALUE);
						System.out.println("Speed: " + getSpeed());
					} else {
						modifyDesiredSpeed2(0);
					}
					vc.acceleration = 0;
				} else if (vc.acceleration == -1) {
					modifyDesiredSpeed2(0.000000001);
					System.out.println("Speed: " + getSpeed());
					vc.acceleration = 0;
				} else {
					modifyDesiredSpeed2(0);
				}

				// if (this.longitudinalModel instanceof IDM) {
				// IDM IDMModel = (IDM) this.longitudinalModel;
				// System.out.println("vc.acceleration = 0 : " + IDMModel.getDesiredSpeed() + "
				// " + getSpeed());
				// }

				if (vc.laneChange < 0) {
					vc.laneChange = 0;
					if (getContinousLane() % 1 == 0) {
						modifiedDesiredLane(BehaviorEnum.rightToleft);
					}
				}

				if (vc.laneChange > 0) {
					vc.laneChange = 0;
					if (getContinousLane() % 1 == 0) {
						modifiedDesiredLane(BehaviorEnum.leftToright);
					}

				}
			}
		}
	}

	/*
	 * if (myNewSpeed > getDesiredSpeed()){ modifyDesiredSpeed(0.2);
	 * setColorObject(Color.red); } else if (myNewSpeed <
	 * selfPerception.getSpeed()){ modifyDesiredSpeed(-0.2);
	 * setColorObject(Color.blue); } else setColorObject(Color.green); // stable
	 * 
	 * }
	 */
	/*
	 * increase or decrease its desired speed
	 */
	private void modifyDesiredSpeed(double ns) {
		System.out.println("modifyDesiredSpeed ");
		if (this.longitudinalModel instanceof IDM) {
			IDM IDMModel = (IDM) this.longitudinalModel;
			if (ns > 0 && IDMModel.getDesiredSpeed() < (160.0d / 3.6d)
					|| (ns < 0 && IDMModel.getDesiredSpeed() > (10.0d / 3.6d))) {
				// System.out.println(" avant modification : " + IDMModel.getDesiredSpeed());
				double currentDesiredSpeed = IDMModel.getDesiredSpeed() * (1 + ns);
				double ancien = IDMModel.getDesiredSpeed();
				if (ns > 0)
					System.out.println(getId() + " accélère " + ancien + " to " + currentDesiredSpeed);
				else
					System.out.println(getId() + " ralenti " + ancien + " to " + currentDesiredSpeed);
				IDMModel.setDesiredSpeed(currentDesiredSpeed);
				System.out.println(" après modification : " + IDMModel.getDesiredSpeed());
			}
		}
	}

	private void modifyDesiredSpeed2(double ns) {
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
	private void modifiedDesiredLane(BehaviorEnum behavior) {
		switch (behavior) {
		case leftToright:
			setManualLaneCange(LaneChangeDecision.MANDATORY_TO_RIGHT);
			break;
		case rightToleft:
			setManualLaneCange(LaneChangeDecision.MANDATORY_TO_LEFT);
			break;
		}
	}

	public LaneChangeDecision getManualLaneCange() {
		return manualLaneCange;
	}

	public void setManualLaneCange(LaneChangeDecision manualLaneCange) {
		this.manualLaneCange = manualLaneCange;
	}

}
