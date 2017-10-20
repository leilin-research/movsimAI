package org.systemx.project.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.movsim.simulator.vehicles.Vehicle;
import org.systemx.project.BehaviorEnum;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.state.State;

public class Controller {
	
	static VehicleControl vehicleControl = new VehicleControl();
	static ProjectVehicle controlledVehicle;
	static long vcid;
	static Boolean vcActive;


	public static void checkNewVehicle(ProjectVehicle vehicle) {
		
		if(controlledVehicle!=null) {
			if(Double.isNaN(controlledVehicle.getSpeed())) {				
				System.err.println("!!!!!!!!!!RESET!!!!!!!!!!");
				vcActive = false;
			}
		}

		if (vcActive == null || !vcActive) {
			vcid = vehicle.getId();
			controlledVehicle = vehicle;
			vcActive = true;
			vehicleControl = new VehicleControl();
			System.out.println("NEW===============================" + vehicle.getId());
		}
	}
	
	public static void checkVehicleCrash(ProjectVehicle vehicle) {
		if (vcActive != false && vcid == vehicle.getId()) {
			vcActive = false;
			vcid = -1;
			vehicle.resetDesiredSpeed();
			System.out.println("CRASHED===============================" + vehicle.getId());
		}
	}
	

	public static void controlDecision(ProjectVehicle vehicle) {

		if (vcid == vehicle.getId()) {
			vehicle.setColorObject(Color.red);
			vehicle.getCommunicatingVehicles().clear();
					
//			System.out.println(vehicle.getId() + " ====================================");
//			List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
//			for (int i = 0; i < ids.size(); i++) {
//				System.out.println(vehicle.getCommunicatingVehicles().get(ids.get(i)));
//			}	

			if (vehicle.getDistanceToRoadSegmentEnd() < 30) {
				vcActive = false;
				vehicle.resetDesiredSpeed();
				vehicle.setSpeed(vehicle.getSpeedlimit());
				return;
			}

			if (vehicleControl.acceleration == 1) {
				if (vehicle.getSpeed() < 20) {
					vehicle.modifyDesiredSpeed(20);
					System.out.println("incSpeed: " + vehicle.getSpeed());
				} else {
					vehicle.modifyDesiredSpeed(0);
				}
				vehicleControl.acceleration = 0;
			} else if (vehicleControl.acceleration == -1) {
				if(vehicle.getSpeed() >0) {
					vehicle.modifyDesiredSpeed(0.000000001);
					System.out.println("decSpeed: " + vehicle.getSpeed());
				}
				vehicleControl.acceleration = 0;
			} else {
				vehicle.modifyDesiredSpeed(0);
			}

			if (vehicleControl.laneChange < 0) {
				vehicleControl.laneChange = 0;
				if (vehicle.getContinousLane() % 1 == 0) {
					if (vehicle.getLane() > 0) {
						vehicle.modifiedDesiredLane(BehaviorEnum.rightToleft);
					}
				}
			}

			if (vehicleControl.laneChange > 0) {
				vehicleControl.laneChange = 0;
				if (vehicle.getContinousLane() % 1 == 0) {
					vehicle.modifiedDesiredLane(BehaviorEnum.leftToright);
				}

			}

		}
	}
	
	void getCurrentState() {
		
	}

}
