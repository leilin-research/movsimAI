package org.systemx.project.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.movsim.simulator.roadnetwork.SpeedLimit;
import org.movsim.simulator.vehicles.Vehicle;
import org.systemx.project.BehaviorEnum;
import org.systemx.project.ProjectSensedVehicle;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.QLearning;
import org.systemx.qlearning.state.Action;
import org.systemx.qlearning.state.CarState;
import org.systemx.qlearning.state.State;
import org.systemx.qlearning.state.StatesListGroup;

import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class Controller {

	static VehicleControl vehicleControl = new VehicleControl();
	static ProjectVehicle controlledVehicle;
	static long vcid;
	static Boolean vcActive;
	static QLearning qLearning = new QLearning(false);

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
			vehicle.resetDesiredSpeed();
			QLearning.realTimeCalculateQCrash();
		}
	}

	public static void controlDecision(ProjectVehicle vehicle) {

		if (vcid == vehicle.getId()) {
			vehicle.setColorObject(Color.green);

			State state = getCurrentState(vehicle);
			
			if (vehicleControl.isActionExecuted()) {
				Action action = qLearning.realTimeCalculateQ(state);
				//Action action = qLearning.realTimeTestQ(state);
				vehicleControl.executeAction(action, vehicle);
			}

			if (vehicle.getDistanceToRoadSegmentEnd() < 30) {
				vcActive = false;
				vehicle.resetDesiredSpeed();
				vehicle.setSpeed(vehicle.getSpeedlimit());
				qLearning.realTimeCalculateQReset();
				return;
			}
			
			vehicleControl.machineControl(vehicle, qLearning.speedLimit);
		}else {
			List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
			for (int i = 0; i < ids.size(); i++) {
				SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
				if (sv instanceof ProjectSensedVehicle) {
					long id = ((ProjectSensedVehicle) sv).getSenderId();
					if(id ==vcid) {
						vehicle.setColorObject(Color.white);
					}
				}
			}
		}
	}

	private static State getCurrentState(ProjectVehicle vehicle) {
		
		CarState myCar = new CarState(vehicle.getLane(), 0, (int) vehicle.getSpeed());

		List<CarState> adjacentCars = new ArrayList<CarState>(); 
		
		List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {
				int lane = ((ProjectSensedVehicle) sv).getSenderLane();
				int position = (int) (((ProjectSensedVehicle) sv).getSenderPosition() - vehicle.getFrontPosition());
				int speed = (int) ((ProjectSensedVehicle) sv).getSenderSpeed();
				adjacentCars.add(new CarState(lane, position, speed));
			}
		}
		
		State state = new State(myCar,adjacentCars);
		return state;
	}
}
