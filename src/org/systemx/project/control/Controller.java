package org.systemx.project.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.systemx.project.ProjectSensedVehicle;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.QLearning;
import org.systemx.qlearning.state.Action;
import org.systemx.qlearning.state.AdjacentCarState;
import org.systemx.qlearning.state.AgentCarState;
import org.systemx.qlearning.state.State;
import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class Controller {

	static VehicleControl vehicleControl = new VehicleControl();
	static ProjectVehicle controlledVehicle;
	static long vcid;
	static Boolean vcActive;

	static QLearning qLearning = new QLearning();
	static boolean AIControl = true;

	public static void checkNewVehicle(ProjectVehicle vehicle) {
		if (vcActive == null || !vcActive) {
			vcid = vehicle.getId();
			controlledVehicle = vehicle;
			vcActive = true;
			vehicleControl = new VehicleControl();
		}
	}

	public static void checkVehicleCrash(ProjectVehicle vehicle) {
		if (vcActive != false && vcid == vehicle.getId()) {
			vcActive = false;
			vcid = -1;
			vehicle.resetDesiredSpeedToLimit();
			if (AIControl) {
				QLearning.realTimeCalculateQCrash();
			}
		}
	}

	public static void controlDecision(ProjectVehicle vehicle) {

		if (vcid == vehicle.getId()) {
			vehicle.setColorObject(Color.green);

			State state = getCurrentState(vehicle);

			if (vehicleControl.isActionExecuted()) {
				if (AIControl) {
					Action action = QLearning.realTimeCalculateQ(state);
					vehicleControl.executeAction(action, vehicle);
				}
			}

			if (vehicle.getDistanceToRoadSegmentEnd() < 30) {
				vcActive = false;
				vehicle.resetDesiredSpeedToLimit();
				vehicle.setSpeed(vehicle.getSpeedlimit());
				if (AIControl) {
					QLearning.realTimeCalculateQReset();
				}
				return;
			}

			vehicleControl.machineControl(vehicle, QLearning.numberOfLanes, QLearning.speedLimit);
		} else {
			otherCarsDecision(vehicle);
		}
	}

	private static void otherCarsDecision(ProjectVehicle vehicle) {
		List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {
				long id = ((ProjectSensedVehicle) sv).getSenderId();
				if (id == vcid) {
					if (Math.abs(((ProjectSensedVehicle) sv).getSenderLane() - vehicle.getLane()) <2) {
						if(Math.abs(((ProjectSensedVehicle) sv).getSenderPosition() - vehicle.getFrontPosition()) <50) {
							vehicle.setColorObject(Color.white);
						}
					}
				}
			}
		}
		boolean frontCarTooClose = false;
		boolean rearCarTooClose = false;

		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {

				int lane = ((ProjectSensedVehicle) sv).getSenderLane();
				int position = (int) (((ProjectSensedVehicle) sv).getSenderPosition() - vehicle.getFrontPosition());
				int speed = (int) ((ProjectSensedVehicle) sv).getSenderSpeed();
				long acc = (long) ((ProjectSensedVehicle) sv).getSenderAcceleration();

				if (lane == vehicle.getLane()) {
					
					if (position > 0 && position < 5) {
						if (speed < vehicle.getSpeed()) {
							frontCarTooClose = true;
						}
					}
					if (position < 0 && position > -5) {
						if (speed > vehicle.getSpeed()) {
							rearCarTooClose = true;
						}
					}
				}
			}
		}

		if (frontCarTooClose) {
			vehicle.setColorObject(Color.red);
			vehicle.modifyDesiredSpeed(0.000000001, true, true);
		} else {
			vehicle.resetDesiredSpeed();			
		}

	}

	private static State getCurrentState(ProjectVehicle vehicle) {
		AgentCarState myCar = new AgentCarState(vehicle.getLane(), (int) vehicle.getSpeed());
		
		List<AdjacentCarState> adjacentCars = new ArrayList<AdjacentCarState>();
		
		List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {
				int lane = ((ProjectSensedVehicle) sv).getSenderLane() - vehicle.getLane();
				int position = (int) (((ProjectSensedVehicle) sv).getSenderPosition() - vehicle.getFrontPosition());
				int speed = (int) ((ProjectSensedVehicle) sv).getSenderSpeed();
				
				if(Math.abs(lane) < 2) {
					if(Math.abs(position)<50) {
						adjacentCars.add(new AdjacentCarState(lane, position, speed));
					}
				}
			}
		}

		State state = new State(myCar, adjacentCars);
		return state;
	}

}
