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
import org.systemx.qlearning.commun.Action;
import org.systemx.qlearning.state.CarState;
import org.systemx.qlearning.state.State;

import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class Controller {

	static VehicleControl vehicleControl = new VehicleControl();
	static ProjectVehicle controlledVehicle;
	static long vcid;
	static Boolean vcActive;

	static QLearning qLearning = new QLearning();

	public static void checkNewVehicle(ProjectVehicle vehicle) {

		// if (controlledVehicle != null) {
		// if (Double.isNaN(controlledVehicle.getSpeed())) {
		// System.err.println("!!!!!!!!!!RESET!!!!!!!!!!");
		// vcActive = false;
		// }
		// }

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
			QLearning.realTimeCalculateQCrash(-10000);
		}
	}

	public static void controlDecision(ProjectVehicle vehicle) {

		if (vcid == vehicle.getId()) {
			vehicle.setColorObject(Color.red);

			State state = getCurrentState(vehicle);

			if (vehicleControl.isActionExecuted()) {
				Action action = qLearning.realTimeCalculateQ(state, vehicle.getSpeed());
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
		}
	}

	private static State getCurrentState(ProjectVehicle vehicle) {
		State state = new State();
		state.setMyCar(new CarState(vehicle.getLane(), 0, (int) vehicle.getSpeed()));

		List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {
				int lane = ((ProjectSensedVehicle) sv).getSenderLane();
				int position = (int) (((ProjectSensedVehicle) sv).getSenderPosition() - vehicle.getFrontPosition());
				int speed = (int) ((ProjectSensedVehicle) sv).getSenderSpeed();
				state.getAdjacentCars().add(new CarState(lane, position, speed));
			}
		}
		return state;
	}

	static State lastState;

	private static boolean stateChanged(State state) {
		if (lastState == null) {
			lastState = new State(state);
			return true;
		} else {
			if (lastState.matchesState(state)) {
				return false;
			} else {
				lastState = new State(state);
				return true;
			}
		}
	}

}
