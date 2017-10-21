package org.systemx.project.control;

import org.systemx.project.BehaviorEnum;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.commun.Action;

public class VehicleControl {

	public static int acceleration;
	public static int laneChange;

	static boolean actionExecuted;
	static Action currentAction;
	static int oldLane;
	static int oldSpeed;

	public VehicleControl() {
		super();
		acceleration = 0;
		laneChange = 0;
		actionExecuted = true;
		oldLane = 0;
		oldSpeed = 0;
	}

	public void moveRight() {
		laneChange = 1;
	}

	public void moveLeft() {
		laneChange = -1;
	}

	public void moveDown() {
		acceleration = -1;
	}

	public void moveUp() {
		acceleration = 1;
	}

	@Override
	public String toString() {
		return "vehicleControl [acceleration=" + acceleration + " lane=" + laneChange + "]";
	}

	void manualControl(ProjectVehicle vehicle, int speedLimit) {
		if (acceleration == 1) {
			if (vehicle.getSpeed() < speedLimit) {
				vehicle.modifyDesiredSpeed(speedLimit);
			} else {
				vehicle.modifyDesiredSpeed(0);
			}
			acceleration = 0;
		} else if (acceleration == -1) {
			if (vehicle.getSpeed() > 0) {
				vehicle.modifyDesiredSpeed(0.000000001);
			}
			acceleration = 0;
		} else {
			vehicle.modifyDesiredSpeed(0);
		}

		if (laneChange < 0) {
			laneChange = 0;
			if (vehicle.getContinousLane() % 1 == 0) {
				if (vehicle.getLane() > 0) {
					vehicle.modifiedDesiredLane(BehaviorEnum.rightToleft);
				}
			}
		}
		if (laneChange > 0) {
			laneChange = 0;
			if (vehicle.getContinousLane() % 1 == 0) {
				vehicle.modifiedDesiredLane(BehaviorEnum.leftToright);
			}
		}
	}

	public void executeAction(Action action, ProjectVehicle vehicle) {
		actionExecuted = false;
		currentAction = action;
		oldLane = vehicle.getLane();
		oldSpeed = (int) vehicle.getSpeed();
	}

	void machineControl(ProjectVehicle vehicle, int speedLimit) {
		if (!actionExecuted) {
			switch (currentAction) {
			case goLeft:
				if(vehicle.getLane() == oldLane) {
					laneChange = -1;
				}else if(vehicle.getLane() == oldLane-1){
					if(vehicle.getContinousLane() % 1 == 0) {
						actionExecuted = true;
					}
				}
				break;
			case goRight:
				if(vehicle.getLane() == oldLane) {
					laneChange = 1;
				}else if(vehicle.getLane() == oldLane+1){
					if(vehicle.getContinousLane() % 1 == 0) {
						actionExecuted = true;
					}
				}
				break;
			case incSpeed:
				if(oldSpeed == (int) vehicle.getSpeed()) {
					acceleration = 1;
				}else{
					actionExecuted = true;
				}
				break;
			case decSpeed:
				if(oldSpeed == (int) vehicle.getSpeed()) {
					acceleration = -1;
				}else{
					actionExecuted = true;
				}
				break;
			case noAction:
				actionExecuted = true;
				break;

			default:
				break;
			}
			
		}

		if(!actionExecuted) {
			manualControl(vehicle, speedLimit);
		}
	}

	public static boolean isActionExecuted() {
		return actionExecuted;
	}

}
