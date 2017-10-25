package org.systemx.project.control;

import java.util.ArrayList;
import java.util.List;

import org.systemx.project.BehaviorEnum;
import org.systemx.project.CamMessage;
import org.systemx.project.ProjectSensedVehicle;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.state.Action;

import fr.ifsttar.licit.simulator.agents.perception.representation.SensedVehicle;

public class VehicleControl {

	public static int accelerationControl;
	public static int laneChangeControl;
	
	public static int noActionControl;

	
	public static boolean CamAttackOngoing;
	public static boolean CamAttackControl;
	public static int CamAttackLaneControl;

	private static List<Long> targetIds;
	
	static boolean actionExecuted;
	static Action currentAction;
	static int oldLane;
	static int oldSpeed;

	public VehicleControl() {
		super();
		accelerationControl = 0;
		laneChangeControl = 0;
		
		CamAttackControl = false;
		
		CamAttackLaneControl = 0;

		actionExecuted = true;
		oldLane = 0;
		oldSpeed = 0;
		
		noActionControl = 0;

		targetIds = new ArrayList<Long>();
	}

	public void moveRight() {
		laneChangeControl = 1;
	}

	public void moveLeft() {
		laneChangeControl = -1;
	}

	public void moveDown() {
		accelerationControl = -1;
	}

	public void moveUp() {
		accelerationControl = 1;
	}

	public void camAttackFront() {
		CamAttackControl = true;
		CamAttackLaneControl = 0;
	}

	public void camAttackRight() {
		CamAttackControl = true;
		CamAttackLaneControl = 1;
	}

	public void camAttackLeft() {
		CamAttackControl = true;
		CamAttackLaneControl = -1;
	}

	public void noAction() {
		noActionControl = 10;
	}
	
	@Override
	public String toString() {
		return "vehicleControl [acceleration=" + accelerationControl + " lane=" + laneChangeControl + "]";
	}

	public void executeAction(Action action, ProjectVehicle vehicle) {
		actionExecuted = false;
		currentAction = action;
		oldLane = vehicle.getLane();
		oldSpeed = (int) vehicle.getSpeed();
	}

	void machineControl(ProjectVehicle vehicle, int numberOfLanes, int speedLimit) {
		if (!actionExecuted) {
			switch (currentAction) {
			case goLeft:
				if (vehicle.getLane() == oldLane) {
					laneChangeControl = -1;
				} else if (vehicle.getLane() == oldLane - 1) {
					if (vehicle.getContinousLane() % 1 == 0) {
						actionExecuted = true;
					}
				}
				break;
			case goRight:
				if (vehicle.getLane() == oldLane) {
					laneChangeControl = 1;
				} else if (vehicle.getLane() == oldLane + 1) {
					if (vehicle.getContinousLane() % 1 == 0) {
						actionExecuted = true;
					}
				}
				break;
			case incSpeed:
				if (oldSpeed == (int) vehicle.getSpeed()) {
					accelerationControl = 1;
				} else {
					actionExecuted = true;
				}
				break;
			case decSpeed:
				if (oldSpeed == (int) vehicle.getSpeed()) {
					accelerationControl = -1;
				} else {
					actionExecuted = true;
				}
				break;

			case misbFront:
				if(!CamAttackOngoing) {
					CamAttackOngoing = true;
					camAttackFront();
				}else {
					if(targetIds.size() == 0) {
						CamAttackOngoing = false;
						actionExecuted = true;
					}
				}
				break;
			case misbRight:
				if(!CamAttackOngoing) {
					CamAttackOngoing = true;
					camAttackRight();
				}else {
					if(targetIds.size() == 0) {
						CamAttackOngoing = false;
						actionExecuted = true;
					}
				}
				break;
			case misbLeft:
				if(!CamAttackOngoing) {
					CamAttackOngoing = true;
					camAttackLeft();
				}else {
					if(targetIds.size() == 0) {
						CamAttackOngoing = false;
						actionExecuted = true;
					}
				}
				break;
			case noAction:
				if(noActionControl == 0) {
					noAction();
				}else {
					if(noActionControl == 1) {
						noActionControl = 0;
						actionExecuted = true;
					}else {
						noActionControl--;
					}
				}
				actionExecuted = true;
				break;
			default:
				actionExecuted = true;
				break;
			}
		}

		manualControl(vehicle, numberOfLanes, speedLimit);
	}

	void manualControl(ProjectVehicle vehicle, int numberOfLanes, int speedLimit) {
		if (accelerationControl == 1) {
			if (vehicle.getSpeed() < speedLimit) {
				vehicle.modifyDesiredSpeed(speedLimit, true, false);
			} else {
				vehicle.modifyDesiredSpeed(0, true, false);
			}
			accelerationControl = 0;
		} else if (accelerationControl == -1) {
			if (vehicle.getSpeed() > 0) {
				vehicle.modifyDesiredSpeed(0.000000001, true, false);
			}
			accelerationControl = 0;
		} else {
			vehicle.modifyDesiredSpeed(0, true, false);
		}

		if (laneChangeControl < 0) {
			laneChangeControl = 0;
			if (vehicle.getContinousLane() % 1 == 0) {
				if (vehicle.getLane() > 0) {
					vehicle.modifiedDesiredLane(BehaviorEnum.rightToleft);
				}
			}
		}
		if (laneChangeControl > 0) {
			laneChangeControl = 0;
			if (vehicle.getContinousLane() % 1 == 0) {
				vehicle.modifiedDesiredLane(BehaviorEnum.leftToright);
			}
		}

		if (CamAttackControl) {
			CamAttackControl = false;
			int attackLane = vehicle.getLane() + CamAttackLaneControl;
			if (attackLane >= 1 && attackLane <= numberOfLanes) {
				findTargetCar(vehicle, attackLane);
			}
		}
		sendFaultyCams(vehicle);
	}

	private void findTargetCar(ProjectVehicle vehicle, int targetLane) {
		double targetPosition = -Integer.MAX_VALUE;
		long targetId = -1;
		List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
		for (int i = 0; i < ids.size(); i++) {
			SensedVehicle sv = vehicle.getCommunicatingVehicles().get(ids.get(i));
			if (sv instanceof ProjectSensedVehicle) {
				int lane = ((ProjectSensedVehicle) sv).getSenderLane();
				double position = ((ProjectSensedVehicle) sv).getSenderPosition();
				if (lane == targetLane) {
					if (targetPosition < position) {
						targetPosition = position;
						targetId = ids.get(i);
					}
				}
			}
		}

		if (targetId != -1) {
			if (!targetIds.contains(targetId)) {
				targetIds.add(targetId);
			}
		}
	}

	private void sendFaultyCams(ProjectVehicle vehicle) {
		for (int i = 0; i < targetIds.size(); i++) {
			if (vehicle.getCommunicatingVehicles().containsKey(targetIds.get(i))) {
				SensedVehicle sv = vehicle.getCommunicatingVehicles().get(targetIds.get(i));
				if (sv instanceof ProjectSensedVehicle) {
					if(((ProjectSensedVehicle) sv).getSenderSpeed() < 1) {
						targetIds.remove(i);
						i--;
					}else {
						int targetLane = ((ProjectSensedVehicle) sv).getSenderLane();
						double targetPosition = ((ProjectSensedVehicle) sv).getSenderPosition();
						double targetSpeed = ((ProjectSensedVehicle) sv).getSenderSpeed();
						targetSpeed = (targetSpeed < 5) ? 5 : targetSpeed;
						vehicle.sendMessage(new CamMessage(vehicle.getId(), targetPosition + 5, targetLane, targetSpeed - 5,
								targetIds.get(i)));
					}
				}
			} else {
				targetIds.remove(i);
				i--;
			}
		}
	}

	public boolean isActionExecuted() {
		return actionExecuted;
	}

}
